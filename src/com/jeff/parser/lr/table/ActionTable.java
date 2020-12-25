package com.jeff.parser.lr.table;

import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.util.*;
import java.util.stream.Collectors;

public class ActionTable {

    private final Map<Integer, Map<Symbol, List<Action>>> table = new HashMap<>();
    private final Map<Symbol, Set<Symbol>> followSets;
    private final CanonicalCollection collection;

    public ActionTable(CanonicalCollection collection, Symbol startSymbol, Map<Symbol, Set<Symbol>> followSets) {
        this.collection = collection;
        this.followSets = followSets;

        constructShiftPart();
        constructReducePart(startSymbol);
    }

    public void constructShiftPart(){
        for(int i = 0; i < collection.size(); i++) {
            Closure closure = collection.get(i);
            for(Map.Entry<Symbol, Integer> entry : closure.getGotoEntries()) {
                Symbol nextSymbol = entry.getKey();
                Integer nextState = entry.getValue();
                if (entry.getKey() instanceof Terminal)
                    put(i, nextSymbol, new Action(ActionType.SHIFT, nextState));
            }
        }
    }

    public void constructReducePart(Symbol startSymbol){
        for(int i = 0; i < collection.size(); i++) {
            Closure closure = collection.get(i);
            for(Item item : closure.getItems()) {
                Symbol left = item.getRule().getLeft();
                if(!item.hasNextSymbol())
                    if(item.getRule().getLeft().equals(startSymbol))
                        put(i, Terminal.END, new Action(ActionType.ACCEPT, 0));
                    else
                        for(Symbol follow : followSets.getOrDefault(left, new HashSet<>()))
                            put(i, follow, new Action(ActionType.REDUCE, item.getIndex()));
            }
        }
    }


    public void put(Integer state, Symbol symbol, Action action){
        Map<Symbol, List<Action>> line = table.get(state);
        if(line == null) {
            line = new HashMap<>();
            table.put(state, line);
        }

        List<Action> actionList = line.get(symbol);
        if(actionList == null) {
            actionList = new ArrayList<>();
            line.put(symbol, actionList);
        }
        actionList.add(action);
    }

    public List<Action> get(Integer state, Symbol symbol) {
        return table.getOrDefault(state, new HashMap<>()).get(symbol);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        List<Map.Entry<Integer, Map<Symbol, List<Action>>>> entryList = table.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).collect(Collectors.toList());
        for(Map.Entry<Integer, Map<Symbol, List<Action>>> entry : entryList) {
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
