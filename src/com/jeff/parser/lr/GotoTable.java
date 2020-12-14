package com.jeff.parser.lr;

import com.jeff.parser.Symbol;

import java.util.*;
import java.util.stream.Collectors;

public class GotoTable {

    private Map<Integer, Map<Symbol, Integer>> actionTable = new HashMap<>();

    public void put(Integer state, Symbol symbol, Integer gotoIndex){
        Map<Symbol, Integer> line = actionTable.get(state);
        if(line == null) {
            line = new HashMap<>();
            actionTable.put(state, line);
        }
        line.put(symbol, gotoIndex);
    }

    public Integer get(Integer state, Symbol symbol) {
        return actionTable.getOrDefault(state, new HashMap<>()).get(symbol);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<Integer, Map<Symbol, Integer>> entry : actionTable.entrySet()) {
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
