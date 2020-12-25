package com.jeff.parser.lr.table;

import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;

import java.util.Objects;
import java.util.Optional;

public class Item {
    private final ProductionRule rule;
    private final int index;
    private final int position;

    public Item(int index, int position, ProductionRule rule) {
        this.index = index;
        this.position = position;
        this.rule = rule;
    }

    public boolean hasNextSymbol() {
        return position < rule.getRight().size();
    }

    public Symbol getNextSymbol() {
        return rule.getRight().get(position);
    }

    public Optional<Item> getNextItem() {
        if(hasNextSymbol())
            return Optional.of(new Item(index, position + 1, rule));
        return Optional.empty();
    }

    public ProductionRule getRule() {
        return rule;
    }

    public int getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return index == item.index &&
                position == item.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, position);
    }

    @Override
    public String toString() {
        StringBuffer right = new StringBuffer();
        for(int i = 0; i < rule.getRight().size(); i++) {
            if(i == position)
                right.append(". ");
            right.append(rule.getRight().get(i).getValue()).append(" ");
        }
        if(position >= rule.getRight().size())
            right.append(". ");
        return rule.getLeft().getValue() + " -> " + right.toString();
    }
}
