package com.jeff.parser;

/**
 * 终结符和非终结符均继承Symbol
 */
public class NonTerminal extends Symbol{
    // 非终结符的构造函数
    public NonTerminal(String value) {
        super(value);
    }


    public NonTerminal(Symbol symbol) {
        super(symbol.getValue());
    }


    @Override
    public String toString() {
        return "\"" + getValue() + "\"";
    }
}
