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

    public static List<ProductionRule> getProductionRulesByStringList(List<String> lines, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals){
        List<ProductionRule> rules = new ArrayList<>();
        for(String line : lines)
            generateProductionRule(line, terminals, nonTerminals).ifPresent(rules::add);
        return rules;
    }

    public static Optional<ProductionRule> generateProductionRule(String line, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals) {
        List<String> tokens = Arrays.asList(line.split("\\s+"));
        if(tokens.size() < 2)
            return Optional.empty();
        String leftStr = tokens.get(0);
        Optional<Symbol> leftOpt = getSymbolByString(leftStr, terminals, nonTerminals);
        List<String> rightTokens = tokens.subList(1, tokens.size());
        List<Symbol> right = new ArrayList<>();
        for(String str : rightTokens) {
            Optional<Symbol> symbolOpt = getSymbolByString(str, terminals, nonTerminals);
            symbolOpt.ifPresent(right::add);
        }
        return leftOpt.map(symbol -> new ProductionRule((NonTerminal) symbol, right));
    }

    private static Optional<Symbol> getSymbolByString(String str, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals) {
        if (str.equals("~")) {
            return Optional.of(Symbol.EMPTY);
        } else if (nonTerminals.containsKey(new NonTerminal(str))) {
            NonTerminal nonTerminal = new NonTerminal(str);
            return Optional.of(nonTerminals.getOrDefault(nonTerminal, nonTerminal));        // 去重，防止出现多个Symbol相同值但不是同一个对象的情况
        } else {
            Terminal terminal = new Terminal(str);
            return Optional.of(terminals.getOrDefault(terminal, terminal));                 // 去重，防止出现多个Symbol相同值但不是同一个对象的情况
        }
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
            rightString.append(symbol.getValue()).append(" ");
        }
        return left.getValue() + " => " + rightString.toString();
    }
}
