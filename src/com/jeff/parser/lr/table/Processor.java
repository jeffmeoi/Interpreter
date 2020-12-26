package com.jeff.parser.lr.table;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.util.*;

public class Processor {

    private final Symbol startSymbol;
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;
    private final List<ProductionRule> rules;

    private final HashMap<Symbol, Set<Symbol>> firstSets = new HashMap<>();
    private final HashMap<Symbol, Set<Symbol>> followSets = new HashMap<>();

    public Processor(Symbol startSymbol, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals, List<ProductionRule> rules) {
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;
    }


    private Set<Symbol> computeFirstSet(List<Symbol> symbols){
        Set<Symbol> firstSet = new HashSet<>();
        if(symbols.isEmpty())
            return firstSet;
        firstSet.addAll(firstSets.getOrDefault(symbols.get(0), new HashSet<>()));
        for (int i = 1; i < symbols.size(); i++) {
            if(firstSets.getOrDefault(symbols.get(i-1), new HashSet<>()).contains(Symbol.EMPTY)) {
                firstSet.addAll(firstSets.getOrDefault(symbols.get(i), new HashSet<>()));
            } else break;
        }
        return firstSet;
    }

    public HashMap<Symbol, Set<Symbol>> generateAllFirstSet(){
        // step 1
        for(Terminal terminal : terminals.keySet()){
            Set<Symbol> firstSet = firstSets.getOrDefault(terminal, new HashSet<>());
            firstSets.put(terminal, firstSet);
            firstSet.add(terminal);
        }
        // step 2
        for(ProductionRule rule : rules) {
            if(rule.isDeriveEmpty()) {
                NonTerminal left = rule.getLeft();
                Set<Symbol> firstSet = firstSets.getOrDefault(left, new HashSet<>());
                firstSets.put(left, firstSet);
                firstSet.add(Symbol.EMPTY);
            }
        }
        // step 3
        while (true) {
            int modifyCnt = 0;
            for (ProductionRule rule : rules) {
                Symbol left = rule.getLeft();
                List<Symbol> right = rule.getRight();
                Set<Symbol> firstSet = firstSets.getOrDefault(left, new HashSet<>());
                firstSets.put(left, firstSet);
                int originSize = firstSet.size();
                firstSet.addAll(computeFirstSet(right));
                modifyCnt += firstSet.size() - originSize;
            }
            if(modifyCnt == 0)
                break;
        }
        // System.out.println(firstSets);
        return firstSets;
    }

    public HashMap<Symbol, Set<Symbol>> generateAllFollowSet(){
        // step 1
        Set<Symbol> followSet = firstSets.getOrDefault(startSymbol, new HashSet<>());
        followSets.put(startSymbol, followSet);
        followSet.add(Terminal.END);

        // step 2
        for(ProductionRule rule : rules) {
            List<Symbol> right = rule.getRight();
            for(int i = 1; i < right.size(); i++) {
                if(right.get(i-1) instanceof NonTerminal) {
                    Set<Symbol> currentFollowSet = followSets.getOrDefault(right.get(i-1), new HashSet<>());
                    followSets.put(right.get(i - 1), currentFollowSet);
                    boolean containEmpty = currentFollowSet.contains(Symbol.EMPTY);
                    currentFollowSet.addAll(computeFirstSet(right.subList(i, right.size())));
                    if (!containEmpty)
                        currentFollowSet.remove(Symbol.EMPTY);
                }
            }
        }

        // step 3
        while (true) {
            int modifyCnt = 0;
            for (ProductionRule rule : rules) {
                Symbol left = rule.getLeft();
                List<Symbol> right = rule.getRight();
                Set<Symbol> leftFollowSet = followSets.getOrDefault(left, new HashSet<>());
                followSets.put(left, leftFollowSet);
                if (rule.isDeriveEmpty())
                    continue;
                Symbol last = right.get(right.size() - 1);
                if(last instanceof NonTerminal) {
                    Set<Symbol> lastFollowSet = followSets.getOrDefault(last, new HashSet<>());
                    followSets.put(last, lastFollowSet);
                    int originSize = lastFollowSet.size();
                    lastFollowSet.addAll(leftFollowSet);
                    modifyCnt += lastFollowSet.size() - originSize;
                }
                for(int i = 0; i < right.size() - 1; i++) {
                    if(right.get(i) instanceof NonTerminal && firstSets.getOrDefault(right.get(i+1), new HashSet<>()).contains(Symbol.EMPTY)) {
                        Set<Symbol> temp = followSets.getOrDefault(right.get(i), new HashSet<>());
                        followSets.put(right.get(i), temp);
                        int originSize = temp.size();
                        temp.addAll(leftFollowSet);
                        modifyCnt += temp.size() - originSize;
                    }
                }
            }
            if(modifyCnt == 0)
                break;
        }
//        System.out.println(followSets);
        return followSets;
    }


}
