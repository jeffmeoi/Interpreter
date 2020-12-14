package com.jeff.parser.lr;

import com.jeff.FileUtils;
import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SLRTableParser {

    private final Symbol startSymbol;
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;
    private final List<ProductionRule> rules;
    private final CanonicalCollection collection;


    public SLRTableParser(Symbol startSymbol, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals, List<ProductionRule> rules) throws IOException {

        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;

        Processor processor = new Processor(startSymbol, terminals, nonTerminals, rules);
        processor.computeAllFirstSet();
        processor.computeAllFollowSet();

        for(NonTerminal nonTerminal : nonTerminals.keySet()) {
            System.out.println(nonTerminal);
            System.out.println(nonTerminal.getFollowSet());
        }

        Closure I0 = new Closure(new HashSet<>(Arrays.asList(new Item(0, 0, rules.get(0)))), rules);
        collection = new CanonicalCollection(I0, terminals, nonTerminals);

    }

    public SLRParsingTable parse() {
        ActionTable actionTable = new ActionTable();
        GotoTable gotoTable = new GotoTable();
        List<Closure> closureList = collection.getClosureList();
        // shift
        for(int i = 0; i < closureList.size(); i++) {
            Closure closure = closureList.get(i);
            for(Map.Entry<Symbol, Integer> entry : closure.getGotoTable().entrySet()) {
                if(entry.getKey() instanceof Terminal) {
                    actionTable.put(i, entry.getKey(), new Action(ActionType.SHIFT, entry.getValue()));
                } else {
                    gotoTable.put(i, entry.getKey(), entry.getValue());
                }
            }
        }

        // reduce
        for(int i = 0; i < closureList.size(); i++) {
            Closure closure = closureList.get(i);
            for(Item item : closure.getItems()) {
                Symbol left = item.getRule().getLeft();
                for(NonTerminal nonTerminal : nonTerminals.keySet()) {
                    if (nonTerminal.equals(left)) {
                        left = nonTerminal;
                        break;
                    }
                }
                List<Symbol> right = item.getRule().getRight();
                if(item.getPosition() == right.size() || right.get(0).equals(Symbol.EMPTY)) {
                    if(item.getRule().getLeft().equals(startSymbol)) {
                        actionTable.put(i, terminals.get(Terminal.END), new Action(ActionType.ACCEPT, 0));
                    } else {
                        for(Symbol follow : left.getFollowSet()) {
                            actionTable.put(i, follow, new Action(ActionType.REDUCE, item.getIndex()));
                        }
                    }
                }
            }
        }

        System.out.println(actionTable);
        System.out.println(gotoTable);

        return new SLRParsingTable(actionTable, gotoTable);
    }


}
