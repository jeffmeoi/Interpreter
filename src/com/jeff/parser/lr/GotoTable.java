package com.jeff.parser.lr;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.Symbol;

import java.util.*;
import java.util.stream.Collectors;

public class GotoTable {

    private Map<Integer, Map<Symbol, Integer>> table = new HashMap<>();

    public GotoTable(CanonicalCollection collection) {
        construct(collection);
    }

    public void construct(CanonicalCollection collection){
        for(int i = 0; i < collection.size(); i++) {
            Closure closure = collection.get(i);
            for(Map.Entry<Symbol, Integer> entry : closure.getGotoEntries()) {
                if(entry.getKey() instanceof NonTerminal) {
                    put(i, entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void put(Integer state, Symbol symbol, Integer gotoIndex){
        Map<Symbol, Integer> line = table.get(state);
        if(line == null) {
            line = new HashMap<>();
            table.put(state, line);
        }
        line.put(symbol, gotoIndex);
    }

    public Integer get(Integer state, Symbol symbol) {
        return table.getOrDefault(state, new HashMap<>()).get(symbol);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        List<Map.Entry<Integer, Map<Symbol, Integer>>> entryList = table.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).collect(Collectors.toList());
        for(Map.Entry<Integer, Map<Symbol, Integer>> entry : entryList) {
            Map<Symbol, Integer> line = entry.getValue();
            sb.append(entry.getKey() + "\t");
            for(Map.Entry<Symbol, Integer> entries : line.entrySet()) {
                sb.append(entries.getKey().getValue() + ":" + entries.getValue() + "\t");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
