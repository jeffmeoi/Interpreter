package com.jeff.parser;

import java.util.Arrays;
import java.util.List;

/**
 * 终结符和非终结符均继承Symbol符号
 */
public class Terminal extends Symbol{

    // 定义终结符
    public static final Terminal OPEN_BRACE = new Terminal("{");
    public static final Terminal CLOSE_BRACE = new Terminal("}");
    public static final Terminal IF = new Terminal("if");
    public static final Terminal OPEN_PARENTHESES = new Terminal("(");
    public static final Terminal CLOSE_PARENTHESES = new Terminal(")");
    public static final Terminal THEN = new Terminal("then");
    public static final Terminal ELSE = new Terminal("else");
    public static final Terminal WHILE = new Terminal("while");
    public static final Terminal ID = new Terminal("ID");
    public static final Terminal IS = new Terminal("=");
    public static final Terminal SEMICOLON = new Terminal(";");
    public static final Terminal LESS_THAN = new Terminal("<");
    public static final Terminal GREATER_THAN = new Terminal(">");
    public static final Terminal LESS_THAN_OR_EQUAL = new Terminal("<=");
    public static final Terminal GREATER_THAN_OR_EQUAL = new Terminal(">=");
    public static final Terminal EQUAL = new Terminal("==");
    public static final Terminal PLUS = new Terminal("+");
    public static final Terminal MINUS = new Terminal("-");
    public static final Terminal MULTIPLY = new Terminal("*");
    public static final Terminal DIVIDE = new Terminal("/");
    public static final Terminal NUM = new Terminal("NUM");

    // 终结符列表
    public static final List<Terminal> terminalList = Arrays.asList(OPEN_BRACE, CLOSE_BRACE, IF, OPEN_PARENTHESES,
            CLOSE_PARENTHESES, THEN, ELSE, WHILE, ID, IS, SEMICOLON, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL,
            GREATER_THAN_OR_EQUAL, EQUAL, PLUS, MINUS, MULTIPLY, DIVIDE, NUM);

    static {
        Symbol.symbolList.addAll(terminalList);
    }

    public Terminal(String value) {
        super(value);
    }


    @Override
    public String toString() {
        return "Terminal:" + getValue();
    }
}
