package com.jeff.parser;

import java.util.*;

/**
 * 符号，含终结符和非终结符
 */
public class Symbol {

    public static final Symbol EMPTY = new Symbol("\u03B5");    // epsilon的utf8编码

    private final String value;

    public String getValue() {
        return value;
    }

    public Symbol(String value) {
        this.value = value;
    }

    public static Optional<Symbol> getSymbolByString(String str, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(value, symbol.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
