package com.jeff.llparser;

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
    public static final Terminal EMPTY = new Terminal("\u03B5");    // epsilon的utf8编码

    public static final Terminal END = new Terminal("$");

    // 终结符列表
    private static final List<Terminal> terminalList = Arrays.asList(OPEN_BRACE, CLOSE_BRACE, IF, OPEN_PARENTHESES,
            CLOSE_PARENTHESES, THEN, ELSE, WHILE, ID, IS, SEMICOLON, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL,
            GREATER_THAN_OR_EQUAL, EQUAL, PLUS, MINUS, MULTIPLY, DIVIDE, NUM, END, EMPTY);

    public Terminal(String value) {
        super(value);
    }

    /**
     * 判断是否为终结符
     * @param token 需要判断的字符串
     * @return true为是，false为否
     */
    public static boolean isTerminal(String token) {
        for(Terminal terminal : terminalList)
            if(terminal.getValue().equals(token))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "Terminal:" + getValue();
    }
}
