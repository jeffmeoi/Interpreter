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

    private void constructShiftPart(){
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

    private void constructReducePart(Symbol startSymbol){
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

    private void checkConflicts(){
        for(Map.Entry<Integer, Map<Symbol, List<Action>>> mapEntry : table.entrySet()) {
            for(Map.Entry<Symbol, List<Action>> entry : mapEntry.getValue().entrySet()) {
                if(entry.getValue() != null && entry.getValue().size() >= 2)
                    throw new SLRActionTableConflictException(mapEntry.getKey(), entry.getKey(), entry.getValue());
            }
        }
    }


    public void put(Integer state, Symbol symbol, Action action){
        Map<Symbol, List<Action>> line = table.computeIfAbsent(state, k -> new HashMap<>());
        List<Action> actionList = line.computeIfAbsent(symbol, k -> new ArrayList<>());
        actionList.add(action);
    }

    public List<Action> get(Integer state, Symbol symbol) {
        return table.getOrDefault(state, new HashMap<>()).get(symbol);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Map.Entry<Integer, Map<Symbol, List<Action>>>> entryList = table.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).collect(Collectors.toList());
        for(Map.Entry<Integer, Map<Symbol, List<Action>>> entry : entryList) {
            Map<Symbol, List<Action>> line = entry.getValue();
            sb.append(entry.getKey()).append("\t");
            for(Map.Entry<Symbol, List<Action>> actionEntry : line.entrySet()) {
                sb.append(actionEntry.getKey()).append(":").append(actionEntry.getValue()).append("\t");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
