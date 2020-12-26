package com.jeff.parser;

import java.util.*;

public class ProductionRule {
    // 产生式左侧必为一个非终结符
    private final NonTerminal left;
    // 产生式右侧为符号的列表，可能为终结符也可能为非终结符
    private final List<Symbol> right;

    public boolean isDerivationBy(Symbol symbol) {
        return left.equals(symbol);
    }

    public boolean isDeriveEmpty() {
        return right.size() == 0 || right.size() == 1 && right.contains(Symbol.EMPTY);
    }

    public ProductionRule(NonTerminal left, List<Symbol> right) {
        this.left = left;
        this.right = right;
    }

    // 根据输入信息生成产生式
    public static Optional<ProductionRule> generateProductionRule(String line, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals) {
        List<String> tokens = Arrays.asList(line.split("\\s+"));
        if(tokens.size() < 2)       // 过滤非法长度的产生式
            return Optional.empty();
        String leftStr = tokens.get(0);       // 每一行的第一个token是左值，其他是右值
        Optional<Symbol> leftOpt = Symbol.getSymbolByString(leftStr, terminals, nonTerminals);
        List<String> rightTokens = tokens.subList(1, tokens.size());

        // 生成产生式右侧的符号列表
        List<Symbol> right = new ArrayList<>();
        for(String str : rightTokens) {
            Optional<Symbol> symbolOpt = Symbol.getSymbolByString(str, terminals, nonTerminals);
            symbolOpt.ifPresent(right::add);
        }
        return leftOpt.map(left -> new ProductionRule((NonTerminal) left, right));
    }

    public NonTerminal getLeft() {
        return left;
    }

    public List<Symbol> getRight() {
        return right;
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
        StringBuilder rightString = new StringBuilder();
        for (Symbol symbol : right) {
            rightString.append(symbol).append(" ");
        }
        return left + " => " + rightString.toString();
    }
}
