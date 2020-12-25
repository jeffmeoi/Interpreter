package com.jeff.parser.lr.table;

import com.jeff.FileUtils;
import com.jeff.StringUtils;
import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SLRTableMaker {

    private final Symbol startSymbol;
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;
    private final List<ProductionRule> rules;

    private HashMap<Symbol, Set<Symbol>> firstSets;
    private HashMap<Symbol, Set<Symbol>> followSets;
    private CanonicalCollection collection;

    public SLRTableMaker(Symbol startSymbol, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals, List<ProductionRule> rules) {
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;

    }
    public SLRTableMaker(String startSymbolFilepath, String terminalFilepath, String nonTerminalFilepath, String ruleFilepath) throws IOException {
        this.terminals = FileUtils.readTerminalsFromFile(terminalFilepath);
        this.nonTerminals = FileUtils.readNonTerminalsFromFile(nonTerminalFilepath);
        this.startSymbol = FileUtils.readStartSymbolFromFile(startSymbolFilepath, nonTerminals);
        this.rules = FileUtils.readProductionRulesFromFile(ruleFilepath, terminals, nonTerminals);
    }




    public SLRParsingTable make() {

        Processor processor = new Processor(startSymbol, terminals, nonTerminals, rules);
        firstSets = processor.computeAllFirstSet();
        followSets = processor.computeAllFollowSet();
        System.out.println("first set");
        for(Map.Entry<Symbol, Set<Symbol>> entry : firstSets.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        System.out.println("follow set");
        for(Map.Entry<Symbol, Set<Symbol>> entry : followSets.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        Item startRule = new Item(0, 0, rules.get(0));
        Set<Item> initialSet = new HashSet<>(Arrays.asList(startRule));
        Closure I0 = new Closure(initialSet, rules);
        collection = new CanonicalCollection(I0, terminals, nonTerminals);
        ActionTable actionTable = new ActionTable(collection, startSymbol, followSets);
        GotoTable gotoTable = new GotoTable(collection);

        return new SLRParsingTable(actionTable, gotoTable);
    }


    public static void main(String[] args) throws IOException {

        SLRTableMaker tableParser = new SLRTableMaker("start-symbol", "terminals", "non-terminals", "rules");
        SLRParsingTable table = tableParser.make();
        System.out.println(table);
    }

}
