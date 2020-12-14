package com.jeff.parser.lr;

import com.jeff.parser.Symbol;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ActionTable {

    private Map<Integer, Map<Symbol, List<Action>>> actionTable = new HashMap<>();

    public void put(Integer state, Symbol symbol, Action action){
        Map<Symbol, List<Action>> line = actionTable.get(state);
        if(line == null) {
            line = new HashMap<>();
            actionTable.put(state, line);
        }

        List<Action> actionList = line.get(symbol);
        if(actionList == null) {
            actionList = new ArrayList<>();
            line.put(symbol, actionList);
        }
        actionList.add(action);
    }

    public List<Action> get(Integer state, Symbol symbol) {
        return actionTable.getOrDefault(state, new HashMap<>()).get(symbol);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<Integer, Map<Symbol, List<Action>>> entry : actionTable.entrySet()) {
            Map<Symbol, List<Action>> line = entry.getValue();
            sb.append(entry.getKey() + "\t");
            for(Map.Entry<Symbol, List<Action>> actionEntry : line.entrySet()) {
                sb.append(actionEntry.getKey().getValue() + ":" + actionEntry.getValue() + "\t");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
