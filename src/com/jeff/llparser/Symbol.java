package com.jeff.llparser;

import java.util.Objects;

/**
 * 符号，含终结符和非终结符
 */
public class Symbol {
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
}
