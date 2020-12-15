package com.jeff.parser.lr;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.io.IOException;
import java.util.*;

public class SLRTableMaker {

    private final Symbol startSymbol;
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;
    private final CanonicalCollection collection;


    public SLRTableMaker(Symbol startSymbol, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals, List<ProductionRule> rules) {

        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;

//        for(NonTerminal nonTerminal : nonTerminals.keySet()) {
//            System.out.println(nonTerminal);
//            System.out.println(nonTerminal.getFollowSet());
//        }

        Item startRule = new Item(0, 0, rules.get(0));
        Set<Item> initialSet = new HashSet<>(Arrays.asList(startRule));
        Closure I0 = new Closure(initialSet, rules);
        collection = new CanonicalCollection(I0, terminals, nonTerminals);
    }


    public SLRParsingTable make() {
        ActionTable actionTable = new ActionTable(collection, startSymbol);
        GotoTable gotoTable = new GotoTable(collection);

//        System.out.println(actionTable);
//        System.out.println(gotoTable);

        return new SLRParsingTable(actionTable, gotoTable);
    }


}
