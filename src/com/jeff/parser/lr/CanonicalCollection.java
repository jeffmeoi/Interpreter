package com.jeff.parser.lr;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.util.*;
import java.util.stream.Collectors;

public class CanonicalCollection {

    private final List<Closure> closureList;

    public CanonicalCollection(Closure I0, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals) {
        Map<Closure, Integer> closureMap = new HashMap<>();
        Set<Symbol> symbols = new HashSet<>(terminals.keySet());
        symbols.addAll(nonTerminals.keySet());
        closureMap.put(I0, closureMap.size());

        LinkedList<Closure> queue = new LinkedList<>();
        queue.add(I0);
        while(!queue.isEmpty()) {
            Closure first = queue.poll();
            for(Symbol symbol : symbols) {
                Closure gotoClosure = first.gotoNext(symbol);
                Optional.ofNullable(gotoClosure).ifPresent(closure -> {
                    int index = closureMap.size();
                    Integer gotoIndex = closureMap.putIfAbsent(gotoClosure, index);
                    if(gotoIndex == null) {
                        gotoIndex = index;
                        queue.push(gotoClosure);
                    }
                    first.setGoto(symbol, gotoIndex);
                });
            }
        }
        closureList = closureMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public List<Closure> getClosureList() {
        return closureList;
    }
}
