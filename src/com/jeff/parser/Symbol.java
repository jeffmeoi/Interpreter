package com.jeff.parser;

import java.util.*;

/**
 * 符号，含终结符和非终结符
 */
public class Symbol {

    public static final Symbol EMPTY = new Symbol("\u03B5");    // epsilon的utf8编码



    private final Set<Symbol> firstSet = new HashSet<>();
    private final Set<Symbol> followSet = new HashSet<>();

    public Set<Symbol> getFirstSet() {
        return firstSet;
    }


    public Set<Symbol> getFollowSet() {
        return followSet;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public Symbol(String value) {
        this.value = value;
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
        return "Symbol:" + getValue();
    }
}