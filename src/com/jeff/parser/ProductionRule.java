package com.jeff.parser;

import java.util.*;

public class ProductionRule {



    // 产生式左侧必为一个非终结符
    private NonTerminal left;
    // 产生式右侧为符号的列表，可能为终结符也可能为非终结符
    private List<Symbol> right;

    public ProductionRule(NonTerminal left, List<Symbol> right) {
        this.left = left;
        this.right = right;
    }

    public NonTerminal getLeft() {
        return left;
    }

    public void setLeft(NonTerminal left) {
        this.left = left;
    }

    public List<Symbol> getRight() {
        return right;
    }

    public void setRight(List<Symbol> right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionRule rule = (ProductionRule) o;
        return Objects.equals(left, rule.left) &&
                Objects.equals(right, rule.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        StringBuffer rightString = new StringBuffer();
        for(int i = 0; i < right.size(); i++) {
            rightString.append(right.get(i).getValue()).append(" ");
        }
        return left.getValue() + " => " + rightString.toString();
    }
}
