package com.jeff.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 终结符和非终结符均继承Symbol
 */
public class NonTerminal extends Symbol{
    // 非终结符的构造函数
    public NonTerminal(String value) {
        super(value);
    }


    @Override
    public String toString() {
        return "\"" + getValue() + "\"";
    }
}
