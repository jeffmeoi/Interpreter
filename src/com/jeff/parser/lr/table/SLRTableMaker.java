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

    public SLRTableMaker(Symbol startSymbol, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals, List<ProductionRule> rules) throws IOException {
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;

    }
    public SLRTableMaker(String startSymbolFilepath, String terminalFilepath, String nonTerminalFilepath, String ruleFilepath) throws IOException {
        this.terminals = readTerminalsFromFile(terminalFilepath);
        this.nonTerminals = readNonTerminalsFromFile(nonTerminalFilepath);
        this.startSymbol = readStartSymbolFromFile(startSymbolFilepath);
        this.rules = readProductionRulesFromFile(ruleFilepath);
    }


    private NonTerminal readStartSymbolFromFile(String filepath) throws IOException {
        String symbol = FileUtils.read(filepath);
        Symbol startSymbol = new NonTerminal(symbol);
        return nonTerminals.get(startSymbol);
    }

    private Map<Terminal, Terminal> readTerminalsFromFile(String filepath) throws IOException {
        Map<Terminal, Terminal> terminals = StringUtils.separateBySpace(FileUtils.read(filepath))
                .stream().map(s -> new Terminal(s))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        Terminal end = new Terminal(Terminal.END);
        terminals.put(end, end);
        return terminals;
    }

    private Map<NonTerminal, NonTerminal> readNonTerminalsFromFile(String filepath) throws IOException {
        Map<NonTerminal, NonTerminal> nonTerminals = StringUtils.separateBySpace(FileUtils.read(filepath))
                .stream().map(s -> new NonTerminal(s))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        return nonTerminals;
    }

    private List<ProductionRule> readProductionRulesFromFile(String filepath) throws IOException {
        return ProductionRule.getProductionRulesByStringList(StringUtils.separateByLine(FileUtils.read(filepath)), terminals, nonTerminals);
    }


    public SLRParsingTable make() {

        Processor processor = new Processor(startSymbol, terminals, nonTerminals, rules);
        firstSets = processor.computeAllFirstSet();
        followSets = processor.computeAllFollowSet();

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
