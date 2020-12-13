package com.jeff.parser;

import java.util.Arrays;
import java.util.List;

/**
 * 终结符和非终结符均继承Symbol
 */
public class NonTerminal extends Symbol{
    // 定义所有的非终结符
    public static final NonTerminal PROGRAM = new NonTerminal("program");
    public static final NonTerminal STMT = new NonTerminal("stmt");
    public static final NonTerminal COMPOUND_STMT = new NonTerminal("compoundstmt");
    public static final NonTerminal STMTS = new NonTerminal("stmts");
    public static final NonTerminal IF_STMT = new NonTerminal("ifstmt");
    public static final NonTerminal WHILE_STMT = new NonTerminal("whilestmt");
    public static final NonTerminal ASSG_STMT = new NonTerminal("assgstmt");
    public static final NonTerminal BOOL_EXPR = new NonTerminal("boolexpr");
    public static final NonTerminal BOOL_OP = new NonTerminal("boolop");
    public static final NonTerminal ARITH_EXPR = new NonTerminal("arithexpr");
    public static final NonTerminal ARITH_EXPR_PRIME = new NonTerminal("arithexprprime");
    public static final NonTerminal MULT_EXPR = new NonTerminal("multexpr");
    public static final NonTerminal MULT_EXPR_PRIME = new NonTerminal("multexprprime");
    public static final NonTerminal SIMPLE_EXPR = new NonTerminal("simpleexpr");

    // 非终结符的列表
    public static final List<NonTerminal> nonTerminalList = Arrays.asList(PROGRAM, STMT, COMPOUND_STMT, STMTS, IF_STMT,
            WHILE_STMT, ASSG_STMT, BOOL_EXPR, BOOL_OP, ARITH_EXPR, ARITH_EXPR_PRIME, MULT_EXPR, MULT_EXPR_PRIME, SIMPLE_EXPR);

    static {
        Symbol.symbolList.addAll(nonTerminalList);
    }

    // 非终结符的构造函数
    public NonTerminal(String value) {
        super(value);
    }


    @Override
    public String toString() {
        return "NonTerminal:" + getValue();
    }
}
