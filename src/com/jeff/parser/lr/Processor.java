package com.jeff.parser.lr;

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

    public Processor(Symbol startSymbol, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals,
                     List<ProductionRule> rules) {
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;
    }


    private Set<Symbol> computeFirstSet(List<Symbol> symbols){
        Set<Symbol> firstSet = new HashSet<>();
        if(symbols.isEmpty())
            return firstSet;
        firstSet.addAll(symbols.get(0).getFirstSet());
        for (int i = 1; i < symbols.size(); i++) {
            if(symbols.get(i-1).getFirstSet().contains(Symbol.EMPTY)) {
                firstSet.addAll(symbols.get(i).getFirstSet());
            } else break;
        }
        return firstSet;
    }

    public void computeAllFirstSet(){
        for(Terminal terminal : terminals.keySet())
            terminal.getFirstSet().clear();
        for(NonTerminal nonTerminal : nonTerminals.keySet())
            nonTerminal.getFirstSet().clear();

        // step 1
        for(Terminal terminal : terminals.keySet()){
            terminal.getFirstSet().add(terminal);
        }
        // step 2
        List<Symbol> emptyList = Collections.singletonList(Symbol.EMPTY);
        for(ProductionRule rule : rules) {
            NonTerminal left = rule.getLeft();
            if(rule.isDeriveEmpty()) {
                left.getFirstSet().add(Symbol.EMPTY);
            }
        }
        // step 3
        while (true) {
            int modifyCnt = 0;
            for (ProductionRule rule : rules) {
                Symbol left = rule.getLeft();
                List<Symbol> right = rule.getRight();
                Set<Symbol> firstSet = left.getFirstSet();
                int originSize = firstSet.size();
                firstSet.addAll(computeFirstSet(right));
                modifyCnt += firstSet.size() - originSize;
            }
            if(modifyCnt == 0)
                break;
        }
    }

    public void computeAllFollowSet(){
        for(Terminal terminal : terminals.keySet())
            terminal.getFollowSet().clear();
        for(NonTerminal nonTerminal : nonTerminals.keySet())
            nonTerminal.getFollowSet().clear();

        // step 1
        startSymbol.getFollowSet().add(terminals.get(Terminal.END));

        // step 2
        for(ProductionRule rule : rules) {
            List<Symbol> right = rule.getRight();
            for(int i = 1; i < right.size(); i++) {
                if(right.get(i-1) instanceof NonTerminal) {
                    boolean containEmpty = right.get(i - 1).getFollowSet().contains(Symbol.EMPTY);
                    right.get(i - 1).getFollowSet().addAll(computeFirstSet(right.subList(i, right.size())));
                    if (!containEmpty)
                        right.get(i - 1).getFollowSet().remove(Symbol.EMPTY);
                }
            }
        }

        // step 3
        while (true) {
            int modifyCnt = 0;
            for (ProductionRule rule : rules) {
                Symbol left = rule.getLeft();
                List<Symbol> right = rule.getRight();
                Set<Symbol> followSet = left.getFollowSet();
                if (right.size() == 0)
                    continue;
                Symbol last = right.get(right.size() - 1);
                if(last instanceof NonTerminal) {
                    int originSize = last.getFollowSet().size();
                    last.getFollowSet().addAll(followSet);
                    modifyCnt += last.getFollowSet().size() - originSize;
                }
                for(int i = 0; i < right.size() - 1; i++) {
                    if(right.get(i) instanceof NonTerminal && right.get(i+1).getFirstSet().contains(Symbol.EMPTY)) {
                        Set<Symbol> temp = right.get(i).getFollowSet();
                        int originSize = temp.size();
                        temp.addAll(followSet);
                        modifyCnt += temp.size() - originSize;
                    }
                }
            }
            if(modifyCnt == 0)
                break;
        }
    }


}
