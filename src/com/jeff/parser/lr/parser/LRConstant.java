package com.jeff.parser.lr.parser;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;
import com.jeff.parser.lr.table.SLRParsingTable;
import com.jeff.parser.lr.table.SLRTableMaker;

import java.io.IOException;
import java.util.*;

public class LRConstant {


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


    // 定义所有的产生式
    public static final ProductionRule P1 = new ProductionRule(PROGRAM, Arrays.asList(COMPOUND_STMT));
    public static final ProductionRule P2 = new ProductionRule(STMT, Arrays.asList(IF_STMT));
    public static final ProductionRule P3 = new ProductionRule(STMT, Arrays.asList(WHILE_STMT));
    public static final ProductionRule P4 = new ProductionRule(STMT, Arrays.asList(ASSG_STMT));
    public static final ProductionRule P5 = new ProductionRule(STMT, Arrays.asList(COMPOUND_STMT));
    public static final ProductionRule P6 = new ProductionRule(COMPOUND_STMT, Arrays.asList(OPEN_BRACE, STMTS, CLOSE_BRACE));
    public static final ProductionRule P7 = new ProductionRule(STMTS, Arrays.asList(STMT, STMTS));
    public static final ProductionRule P8 = new ProductionRule(STMTS, Arrays.asList());
    public static final ProductionRule P9 = new ProductionRule(IF_STMT, Arrays.asList(IF, OPEN_PARENTHESES, BOOL_EXPR, CLOSE_PARENTHESES, THEN, STMT, ELSE, STMT));
    public static final ProductionRule P10 = new ProductionRule(WHILE_STMT, Arrays.asList(WHILE, OPEN_PARENTHESES, BOOL_EXPR, CLOSE_PARENTHESES, STMT));
    public static final ProductionRule P11 = new ProductionRule(ASSG_STMT, Arrays.asList(ID, IS, ARITH_EXPR, SEMICOLON));
    public static final ProductionRule P12 = new ProductionRule(BOOL_EXPR, Arrays.asList(ARITH_EXPR, BOOL_OP, ARITH_EXPR));
    public static final ProductionRule P13 = new ProductionRule(BOOL_OP, Arrays.asList(LESS_THAN));
    public static final ProductionRule P14 = new ProductionRule(BOOL_OP, Arrays.asList(GREATER_THAN));
    public static final ProductionRule P15 = new ProductionRule(BOOL_OP, Arrays.asList(LESS_THAN_OR_EQUAL));
    public static final ProductionRule P16 = new ProductionRule(BOOL_OP, Arrays.asList(GREATER_THAN_OR_EQUAL));
    public static final ProductionRule P17 = new ProductionRule(BOOL_OP, Arrays.asList(EQUAL));
    public static final ProductionRule P18 = new ProductionRule(ARITH_EXPR, Arrays.asList(MULT_EXPR, ARITH_EXPR_PRIME));
    public static final ProductionRule P19 = new ProductionRule(ARITH_EXPR_PRIME, Arrays.asList(PLUS, MULT_EXPR, ARITH_EXPR_PRIME));
    public static final ProductionRule P20 = new ProductionRule(ARITH_EXPR_PRIME, Arrays.asList(MINUS, MULT_EXPR, ARITH_EXPR_PRIME));
    public static final ProductionRule P21 = new ProductionRule(ARITH_EXPR_PRIME, Arrays.asList());
    public static final ProductionRule P22 = new ProductionRule(MULT_EXPR, Arrays.asList(SIMPLE_EXPR, MULT_EXPR_PRIME));
    public static final ProductionRule P23 = new ProductionRule(MULT_EXPR_PRIME, Arrays.asList(MULTIPLY, SIMPLE_EXPR, MULT_EXPR_PRIME));
    public static final ProductionRule P24 = new ProductionRule(MULT_EXPR_PRIME, Arrays.asList(DIVIDE, SIMPLE_EXPR, MULT_EXPR_PRIME));
    public static final ProductionRule P25 = new ProductionRule(MULT_EXPR_PRIME, Arrays.asList());
    public static final ProductionRule P26 = new ProductionRule(SIMPLE_EXPR, Arrays.asList(ID));
    public static final ProductionRule P27 = new ProductionRule(SIMPLE_EXPR, Arrays.asList(NUM));
    public static final ProductionRule P28 = new ProductionRule(SIMPLE_EXPR, Arrays.asList(OPEN_PARENTHESES, ARITH_EXPR, CLOSE_PARENTHESES));

    public static final Symbol startSymbol = PROGRAM;
    public static final Map<Terminal, Terminal> terminals = new HashMap<>();
    public static final Map<NonTerminal, NonTerminal> nonTerminals = new HashMap<>();
    public static final List<ProductionRule> rules = new ArrayList<>();

    static {
        terminals.put(OPEN_BRACE, OPEN_BRACE);
        terminals.put(CLOSE_BRACE, CLOSE_BRACE);
        terminals.put(IF, IF);
        terminals.put(OPEN_PARENTHESES, OPEN_PARENTHESES);
        terminals.put(CLOSE_PARENTHESES, CLOSE_PARENTHESES);
        terminals.put(THEN, THEN);
        terminals.put(ELSE, ELSE);
        terminals.put(WHILE, WHILE);
        terminals.put(ID, ID);
        terminals.put(IS, IS);
        terminals.put(SEMICOLON, SEMICOLON);
        terminals.put(LESS_THAN, LESS_THAN);
        terminals.put(GREATER_THAN, GREATER_THAN);
        terminals.put(LESS_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL);
        terminals.put(GREATER_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL);
        terminals.put(EQUAL, EQUAL);
        terminals.put(PLUS, PLUS);
        terminals.put(MINUS, MINUS);
        terminals.put(MULTIPLY, MULTIPLY);
        terminals.put(DIVIDE, DIVIDE);
        terminals.put(NUM, NUM);
        terminals.put(Terminal.END, Terminal.END);

        nonTerminals.put(PROGRAM, PROGRAM);
        nonTerminals.put(STMT, STMT);
        nonTerminals.put(COMPOUND_STMT, COMPOUND_STMT);
        nonTerminals.put(STMTS, STMTS);
        nonTerminals.put(IF_STMT, IF_STMT);
        nonTerminals.put(WHILE_STMT, WHILE_STMT);
        nonTerminals.put(ASSG_STMT, ASSG_STMT);
        nonTerminals.put(BOOL_EXPR, BOOL_EXPR);
        nonTerminals.put(BOOL_OP, BOOL_OP);
        nonTerminals.put(ARITH_EXPR, ARITH_EXPR);
        nonTerminals.put(ARITH_EXPR_PRIME, ARITH_EXPR_PRIME);
        nonTerminals.put(MULT_EXPR, MULT_EXPR);
        nonTerminals.put(MULT_EXPR_PRIME, MULT_EXPR_PRIME);
        nonTerminals.put(SIMPLE_EXPR, SIMPLE_EXPR);

        rules.addAll(Arrays.asList(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17,
                P18, P19, P20, P21, P22, P23, P24, P25, P26, P27, P28));


    }
}
