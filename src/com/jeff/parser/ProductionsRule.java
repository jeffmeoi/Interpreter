package com.jeff.parser;

import java.util.Arrays;
import java.util.List;

public class ProductionsRule {

    // 定义所有的产生式
    public static final ProductionsRule P1 = new ProductionsRule(NonTerminal.PROGRAM, Arrays.asList(NonTerminal.COMPOUND_STMT));
    public static final ProductionsRule P2 = new ProductionsRule(NonTerminal.STMT, Arrays.asList(NonTerminal.IF_STMT));
    public static final ProductionsRule P3 = new ProductionsRule(NonTerminal.STMT, Arrays.asList(NonTerminal.WHILE_STMT));
    public static final ProductionsRule P4 = new ProductionsRule(NonTerminal.STMT, Arrays.asList(NonTerminal.ASSG_STMT));
    public static final ProductionsRule P5 = new ProductionsRule(NonTerminal.STMT, Arrays.asList(NonTerminal.COMPOUND_STMT));
    public static final ProductionsRule P6 = new ProductionsRule(NonTerminal.COMPOUND_STMT, Arrays.asList(Terminal.OPEN_BRACE, NonTerminal.STMTS, Terminal.CLOSE_BRACE));
    public static final ProductionsRule P7 = new ProductionsRule(NonTerminal.STMTS, Arrays.asList(NonTerminal.STMT, NonTerminal.STMTS));
    public static final ProductionsRule P8 = new ProductionsRule(NonTerminal.STMTS, Arrays.asList(Terminal.EMPTY));
    public static final ProductionsRule P9 = new ProductionsRule(NonTerminal.IF_STMT, Arrays.asList(Terminal.IF, Terminal.OPEN_PARENTHESES, NonTerminal.BOOL_EXPR, Terminal.CLOSE_PARENTHESES, Terminal.THEN, NonTerminal.STMT, Terminal.ELSE, NonTerminal.STMT));
    public static final ProductionsRule P10 = new ProductionsRule(NonTerminal.WHILE_STMT, Arrays.asList(Terminal.WHILE, Terminal.OPEN_PARENTHESES, NonTerminal.BOOL_EXPR, Terminal.CLOSE_PARENTHESES, NonTerminal.STMT));
    public static final ProductionsRule P11 = new ProductionsRule(NonTerminal.ASSG_STMT, Arrays.asList(Terminal.ID, Terminal.IS, NonTerminal.ARITH_EXPR, Terminal.SEMICOLON));
    public static final ProductionsRule P12 = new ProductionsRule(NonTerminal.BOOL_EXPR, Arrays.asList(NonTerminal.ARITH_EXPR, NonTerminal.BOOL_OP, NonTerminal.ARITH_EXPR));
    public static final ProductionsRule P13 = new ProductionsRule(NonTerminal.BOOL_OP, Arrays.asList(Terminal.LESS_THAN));
    public static final ProductionsRule P14 = new ProductionsRule(NonTerminal.BOOL_OP, Arrays.asList(Terminal.GREATER_THAN));
    public static final ProductionsRule P15 = new ProductionsRule(NonTerminal.BOOL_OP, Arrays.asList(Terminal.LESS_THAN_OR_EQUAL));
    public static final ProductionsRule P16 = new ProductionsRule(NonTerminal.BOOL_OP, Arrays.asList(Terminal.GREATER_THAN_OR_EQUAL));
    public static final ProductionsRule P17 = new ProductionsRule(NonTerminal.BOOL_OP, Arrays.asList(Terminal.EQUAL));
    public static final ProductionsRule P18 = new ProductionsRule(NonTerminal.ARITH_EXPR, Arrays.asList(NonTerminal.MULT_EXPR, NonTerminal.ARITH_EXPR_PRIME));
    public static final ProductionsRule P19 = new ProductionsRule(NonTerminal.ARITH_EXPR_PRIME, Arrays.asList(Terminal.PLUS, NonTerminal.MULT_EXPR, NonTerminal.ARITH_EXPR_PRIME));
    public static final ProductionsRule P20 = new ProductionsRule(NonTerminal.ARITH_EXPR_PRIME, Arrays.asList(Terminal.MINUS, NonTerminal.MULT_EXPR, NonTerminal.ARITH_EXPR_PRIME));
    public static final ProductionsRule P21 = new ProductionsRule(NonTerminal.ARITH_EXPR_PRIME, Arrays.asList(Terminal.EMPTY));
    public static final ProductionsRule P22 = new ProductionsRule(NonTerminal.MULT_EXPR, Arrays.asList(NonTerminal.SIMPLE_EXPR, NonTerminal.MULT_EXPR_PRIME));
    public static final ProductionsRule P23 = new ProductionsRule(NonTerminal.MULT_EXPR_PRIME, Arrays.asList(Terminal.MULTIPLY, NonTerminal.SIMPLE_EXPR, NonTerminal.MULT_EXPR_PRIME));
    public static final ProductionsRule P24 = new ProductionsRule(NonTerminal.MULT_EXPR_PRIME, Arrays.asList(Terminal.DIVIDE, NonTerminal.SIMPLE_EXPR, NonTerminal.MULT_EXPR_PRIME));
    public static final ProductionsRule P25 = new ProductionsRule(NonTerminal.MULT_EXPR_PRIME, Arrays.asList(Terminal.EMPTY));
    public static final ProductionsRule P26 = new ProductionsRule(NonTerminal.SIMPLE_EXPR, Arrays.asList(Terminal.ID));
    public static final ProductionsRule P27 = new ProductionsRule(NonTerminal.SIMPLE_EXPR, Arrays.asList(Terminal.NUM));
    public static final ProductionsRule P28 = new ProductionsRule(NonTerminal.SIMPLE_EXPR, Arrays.asList(Terminal.OPEN_PARENTHESES, NonTerminal.ARITH_EXPR, Terminal.CLOSE_PARENTHESES));

    // 产生式左侧必为一个非终结符
    private NonTerminal left;
    // 产生式右侧为符号的列表，可能为终结符也可能为非终结符
    private List<Symbol> right;

    public ProductionsRule(NonTerminal left, List<Symbol> right) {
        this.left = left;
        this.right = right;
    }

    public NonTerminal getLeft() {
        return left;
    }

    public void setLeft(NonTerminal left) {
        this.left = left;
    }

    public List<Symbol> getRight() {
        return right;
    }

    public void setRight(List<Symbol> right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "ProductionsRule{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
