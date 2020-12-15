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
        buildEntireClosure();
    }

    private void buildEntireClosure(){
        while(true) {
            int originCnt = items.size();
            items.addAll(buildMoreItems());
            if(items.size() - originCnt == 0)
                break;
        }
    }

    private Set<Item> buildMoreItems(){
        Set<Item> moreItems = new HashSet<>();
        for(Item item : items) {
            if(!item.hasNextSymbol())   continue;
            Symbol symbol = item.getNextSymbol();
            if(symbol instanceof NonTerminal) {
                for(int i = 0; i < rules.size(); i++) {
                    ProductionRule rule = rules.get(i);
                    if(rule.isDerivationBy(symbol))
                        moreItems.add(new Item(i, 0, rule));
                }
            }
        }
        return moreItems;
    }

    public Optional<Closure> gotoNext(Symbol symbol) {
        Set<Item> gotoItems = new HashSet<>();
        for(Item item : items) {
            if(!item.hasNextSymbol())   continue;
            Symbol s = item.getNextSymbol();
            if(s.equals(symbol)) {
                Optional<Item> nextItemOpt = item.getNextItem();
                nextItemOpt.ifPresent(next -> gotoItems.add(next));
            }
        }
        if(gotoItems.size() == 0)
            return Optional.empty();
        return Optional.of(new Closure(gotoItems, rules));
    }

    public Set<Item> getItems() {
        return items;
    }

    public Set<Map.Entry<Symbol, Integer>> getGotoEntries() {
        return gotoTable.entrySet();
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
