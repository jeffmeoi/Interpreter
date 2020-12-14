package com.jeff.parser.ll;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.util.*;

public class LLParsingTable {

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
    public static final ProductionRule P8 = new ProductionRule(STMTS, Arrays.asList(Symbol.EMPTY));
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
    public static final ProductionRule P21 = new ProductionRule(ARITH_EXPR_PRIME, Arrays.asList(Symbol.EMPTY));
    public static final ProductionRule P22 = new ProductionRule(MULT_EXPR, Arrays.asList(SIMPLE_EXPR, MULT_EXPR_PRIME));
    public static final ProductionRule P23 = new ProductionRule(MULT_EXPR_PRIME, Arrays.asList(MULTIPLY, SIMPLE_EXPR, MULT_EXPR_PRIME));
    public static final ProductionRule P24 = new ProductionRule(MULT_EXPR_PRIME, Arrays.asList(DIVIDE, SIMPLE_EXPR, MULT_EXPR_PRIME));
    public static final ProductionRule P25 = new ProductionRule(MULT_EXPR_PRIME, Arrays.asList(Symbol.EMPTY));
    public static final ProductionRule P26 = new ProductionRule(SIMPLE_EXPR, Arrays.asList(ID));
    public static final ProductionRule P27 = new ProductionRule(SIMPLE_EXPR, Arrays.asList(NUM));
    public static final ProductionRule P28 = new ProductionRule(SIMPLE_EXPR, Arrays.asList(OPEN_PARENTHESES, ARITH_EXPR, CLOSE_PARENTHESES));

    // parsing table
    public static final Map<NonTerminal, Map<Terminal, ProductionRule>> table = new HashMap<>();

    static {
        // 初始化parsing table
        table.put(PROGRAM, new HashMap<>());
        table.put(STMT, new HashMap<>());
        table.put(COMPOUND_STMT, new HashMap<>());
        table.put(STMTS, new HashMap<>());
        table.put(IF_STMT, new HashMap<>());
        table.put(WHILE_STMT, new HashMap<>());
        table.put(ASSG_STMT, new HashMap<>());
        table.put(BOOL_EXPR, new HashMap<>());
        table.put(BOOL_OP, new HashMap<>());
        table.put(ARITH_EXPR, new HashMap<>());
        table.put(ARITH_EXPR_PRIME, new HashMap<>());
        table.put(MULT_EXPR, new HashMap<>());
        table.put(MULT_EXPR_PRIME, new HashMap<>());
        table.put(SIMPLE_EXPR, new HashMap<>());

        table.get(PROGRAM).put(OPEN_BRACE, P1);
        table.get(STMT).put(IF, P2);
        table.get(STMT).put(WHILE, P3);
        table.get(STMT).put(ID, P4);
        table.get(STMT).put(OPEN_BRACE, P5);
        table.get(COMPOUND_STMT).put(OPEN_BRACE, P6);
        table.get(STMTS).put(OPEN_BRACE, P7);
        table.get(STMTS).put(IF, P7);
        table.get(STMTS).put(WHILE, P7);
        table.get(STMTS).put(ID, P7);
        table.get(STMTS).put(CLOSE_BRACE, P8);
        table.get(IF_STMT).put(IF, P9);
        table.get(WHILE_STMT).put(WHILE, P10);
        table.get(ASSG_STMT).put(ID, P11);
        table.get(BOOL_EXPR).put(OPEN_PARENTHESES, P12);
        table.get(BOOL_EXPR).put(ID, P12);
        table.get(BOOL_EXPR).put(NUM, P12);
        table.get(BOOL_OP).put(LESS_THAN, P13);
        table.get(BOOL_OP).put(GREATER_THAN, P14);
        table.get(BOOL_OP).put(LESS_THAN_OR_EQUAL, P15);
        table.get(BOOL_OP).put(GREATER_THAN_OR_EQUAL, P16);
        table.get(BOOL_OP).put(EQUAL, P17);
        table.get(ARITH_EXPR).put(OPEN_PARENTHESES, P18);
        table.get(ARITH_EXPR).put(ID, P18);
        table.get(ARITH_EXPR).put(NUM, P18);
        table.get(ARITH_EXPR_PRIME).put(PLUS, P19);
        table.get(ARITH_EXPR_PRIME).put(MINUS, P20);
        table.get(ARITH_EXPR_PRIME).put(CLOSE_PARENTHESES, P21);
        table.get(ARITH_EXPR_PRIME).put(SEMICOLON, P21);
        table.get(ARITH_EXPR_PRIME).put(LESS_THAN, P21);
        table.get(ARITH_EXPR_PRIME).put(GREATER_THAN, P21);
        table.get(ARITH_EXPR_PRIME).put(LESS_THAN_OR_EQUAL, P21);
        table.get(ARITH_EXPR_PRIME).put(GREATER_THAN_OR_EQUAL, P21);
        table.get(ARITH_EXPR_PRIME).put(EQUAL, P21);
        table.get(MULT_EXPR).put(OPEN_PARENTHESES, P22);
        table.get(MULT_EXPR).put(ID, P22);
        table.get(MULT_EXPR).put(NUM, P22);
        table.get(MULT_EXPR_PRIME).put(MULTIPLY, P23);
        table.get(MULT_EXPR_PRIME).put(DIVIDE, P24);
        table.get(MULT_EXPR_PRIME).put(CLOSE_PARENTHESES, P25);
        table.get(MULT_EXPR_PRIME).put(SEMICOLON, P25);
        table.get(MULT_EXPR_PRIME).put(LESS_THAN, P25);
        table.get(MULT_EXPR_PRIME).put(GREATER_THAN, P25);
        table.get(MULT_EXPR_PRIME).put(LESS_THAN_OR_EQUAL, P25);
        table.get(MULT_EXPR_PRIME).put(GREATER_THAN_OR_EQUAL, P25);
        table.get(MULT_EXPR_PRIME).put(EQUAL, P25);
        table.get(MULT_EXPR_PRIME).put(PLUS, P25);
        table.get(MULT_EXPR_PRIME).put(MINUS, P25);
        table.get(SIMPLE_EXPR).put(ID, P26);
        table.get(SIMPLE_EXPR).put(NUM, P27);
        table.get(SIMPLE_EXPR).put(OPEN_PARENTHESES, P28);
    }

    public Map<NonTerminal, Map<Terminal, ProductionRule>> getTable() {
        return table;
    }
}
