package com.jeff.parser.lr;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;

import java.util.*;

public final class Closure {

    private final Set<Item> items;
    private final List<ProductionRule> rules;
    private final HashMap<Symbol, Integer> gotoTable = new HashMap<>();

    public void setGoto(Symbol symbol, Integer index) {
        gotoTable.putIfAbsent(symbol, index);
    }

    public Closure(Set<Item> initialItems, List<ProductionRule> rules) {
        this.items = new HashSet<>(initialItems);
        this.rules = rules;
        while(true) {
            Set<Item> append = new HashSet<>();
            for(Item item : items) {
                int pos = item.getPosition();
                ProductionRule rule = rules.get(item.getIndex());
                if(pos >= rule.getRight().size())
                    continue;
                Symbol symbol = rule.getRight().get(pos);
                if(symbol instanceof NonTerminal) {
                    for(int i = 0; i < rules.size(); i++) {
                        ProductionRule ele = rules.get(i);
                        if(ele.getLeft().equals(symbol)) {
                            append.add(new Item(i, 0, ele));
                        }
                    }
                }
            }
            int originCnt = items.size();
            items.addAll(append);
            if(items.size() - originCnt == 0)
                break;
        }
    }

    public Closure gotoNext(Symbol symbol) {
        Set<Item> gotoItems = new HashSet<>();
        for(Item item : items) {
            int pos = item.getPosition();
            ProductionRule rule = rules.get(item.getIndex());
            if(pos >= rule.getRight().size())
                continue;
            Symbol s = rule.getRight().get(pos);
            if(s.equals(symbol)) {
                if(s.equals(Symbol.EMPTY))
                    gotoItems.add(new Item(item.getIndex(), 0, rule));
                else
                    gotoItems.add(new Item(item.getIndex(), item.getPosition() + 1, rule));
            }
        }
        if(gotoItems.size() == 0)
            return null;
        return new Closure(gotoItems, rules);
    }

    public Set<Item> getItems() {
        return items;
    }

    public HashMap<Symbol, Integer> getGotoTable() {
        return gotoTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Closure closure = (Closure) o;
        return Objects.equals(items, closure.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("closure:").append(System.lineSeparator());
        for(Item item : items)
            sb.append("\t").append(item).append(System.lineSeparator());
        return sb.toString();

    }
}
