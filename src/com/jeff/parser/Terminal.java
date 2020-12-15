package com.jeff.parser;


/**
 * 终结符和非终结符均继承Symbol符号
 */
public class Terminal extends Symbol{

    public static final Terminal END = new Terminal("$");

    public Terminal(String value) {
        super(value);
    }

    public Terminal(Symbol symbol) {
        super(symbol.getValue());
    }


    @Override
    public String toString() {
        return "\"" + getValue() + "\"";
    }
}
