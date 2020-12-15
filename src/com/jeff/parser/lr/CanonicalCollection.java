package com.jeff.parser.lr;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.util.*;
import java.util.stream.Collectors;

public class CanonicalCollection implements Iterable{

    private final List<Closure> closureList;
    private final HashMap<Closure, Integer> closureStateMap = new HashMap<>();
    private final LinkedList<Closure> queue = new LinkedList<>();
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;

    public CanonicalCollection(Closure I0, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;

        closureStateMap.put(I0, 0);
        queue.add(I0);

        buildCollection();

        this.closureList = closureStateMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private int addClosure(Closure closure) {
        int size = closureStateMap.size();     // 当前项目集数量用于表示新项目集的状态号
        Integer gotoState = closureStateMap.putIfAbsent(closure, size);
        if(gotoState == null) {                 // 如果当前Closure不曾出现过，则加入到Canonical Collection里，并赋予新的状态号
            gotoState = size;                   // 否则说明goto回到的是之前出现过的closure的状态
            queue.push(closure);
        }
        return gotoState;
    }

    private void buildCollection() {
        Set<Symbol> symbols = new HashSet<>(terminals.keySet());
        symbols.addAll(nonTerminals.keySet());

        while(!queue.isEmpty()) {
            Closure front = queue.poll();
            for(Symbol symbol : symbols) {
                Optional<Closure> nextClosure = front.gotoNext(symbol);
                nextClosure.ifPresent(closure -> {
                    int gotoState = addClosure(closure);
                    front.setGoto(symbol, gotoState);
                });
            }
        }
    }

    public int size(){
        return closureList.size();
    }

    public Closure get(int index){
        return closureList.get(index);
    }

    @Override
    public Iterator iterator() {
        return closureList.iterator();
    }
}
