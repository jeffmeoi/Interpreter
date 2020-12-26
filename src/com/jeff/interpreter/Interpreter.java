package com.jeff.interpreter;

public class Interpreter {

    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";
    public static final String IF = "if";
    public static final String OPEN_PARENTHESES = "(";
    public static final String CLOSE_PARENTHESES = ")";
    public static final String THEN = "then";
    public static final String ELSE = "else";
    public static final String ID = "ID";
    public static final String IS = "=";
    public static final String SEMICOLON = ";";
    public static final String LESS_THAN = "<";
    public static final String GREATER_THAN = ">";
    public static final String LESS_THAN_OR_EQUAL = "<=";
    public static final String GREATER_THAN_OR_EQUAL = ">=";
    public static final String EQUAL = "==";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    public static final String INT_NUM = "INTNUM";
    public static final String REAL_NUM = "REALNUM";
    public static final String INT = "int";
    public static final String REAL = "real";
    public static final String END = "$";
    
    private Lex lex;
    private String context;
    private VariableTable table;

    public Interpreter() {

    }

    public void interpret(String context){
        this.context = context;
        this.lex = new Lex(context);
        this.table = buildVariableTable();
        program(true);
    }

    public VariableTable buildVariableTable(){
        Lex lex = new Lex(context);
        VariableTable table = new VariableTable();
        while (lex.hasNext()) {
            Token token = lex.next();
            if (token.getTokenType() == TokenType.IDENTIFIER) {
                table.put(token);
            }
        }
        return table;
    }

    public boolean isSuccess(){
        return lex.current().match(END);
    }

    public void print() {
        System.out.println(table);
    }

    public void program(boolean exec){
        Token token = lex.current();
        if(token.match(OPEN_BRACE) || token.match(INT) || token.match(REAL)) {
            decls();
            compoundstmt(exec);
        }
    }


    private void decls() {
        Token token = lex.current();
        if(token.match(INT) || token.match(REAL)) {
            decl();
            lex.match(SEMICOLON);
            decls();
        }
    }

    private void decl() {
        Token token = lex.current();
        Number syn;
        String id;
        if(token.match(INT)) {
            lex.match(INT);
            id = lex.match(ID);
            lex.match(IS);
            Token current = lex.current();
            syn = current.getLexVal();
            lex.match(INT_NUM);
            table.setValue(id, syn);
        } else if(token.match(REAL)) {
            lex.match(REAL);
            id = lex.match(ID);
            lex.match(IS);
            Token current = lex.current();
            syn = current.getLexVal();
            lex.match(REAL_NUM);
            table.setValue(id, syn);
        }
    }

    public void compoundstmt(boolean exec){
        Token token = lex.current();
        if(token.match(OPEN_BRACE)) {
            lex.match(OPEN_BRACE);
            stmts(exec);
            lex.match(CLOSE_BRACE);
        }
    }

    private void stmts(boolean exec) {
        Token token = lex.current();
        if(token.match(OPEN_BRACE) || token.match(IF) || token.match(ID)) {
            stmt(exec);
            stmts(exec);
        }
    }

    private void stmt(boolean exec) {
        Token token = lex.current();
        if(token.match(OPEN_BRACE)) {
            compoundstmt(exec);
        } else if(token.match(IF)) {
            ifstmt(exec);
        } else if(token.match(ID)) {
            assgstmt(exec);
        }
    }

    private void assgstmt(boolean exec) {
        Token token = lex.current();
        if(token.match(ID)) {
            String id = lex.match(ID);
            lex.match(IS);
            Number syn = arithexpr();
            lex.match(SEMICOLON);
            if(exec) {
                table.setValue(id, syn);
            }
        }
    }

    private Number arithexpr() {
        Token token = lex.current();
        Number syn = null;
        if(token.match(OPEN_PARENTHESES) || token.match(ID) || token.match(INT_NUM) || token.match(REAL_NUM)) {
            Number multexprSyn = multexpr();
            syn = arithexprprime(multexprSyn);
        }
        return syn;
    }

    private Boolean arithexpr(Number inh, String op) {
        Number syn = arithexpr();
        switch (op) {
            case LESS_THAN:
                if(inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() < syn.intValue();
                else
                    return inh.doubleValue() < syn.doubleValue();
            case LESS_THAN_OR_EQUAL:
                if(inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() <= syn.intValue();
                else
                    return inh.doubleValue() <= syn.doubleValue();
            case GREATER_THAN:
                if(inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() > syn.intValue();
                else
                    return inh.doubleValue() > syn.doubleValue();
            case GREATER_THAN_OR_EQUAL:
                if(inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() >= syn.intValue();
                else
                    return inh.doubleValue() >= syn.doubleValue();
            case EQUAL:
                if(inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() == syn.intValue();
                else
                    return inh.doubleValue() == syn.doubleValue();
            default:
                break;
        }
        return null;
    }

    private Number arithexprprime(Number inh) {
        Token token = lex.current();
        Number syn = inh;
        if(token.match(PLUS)) {
            lex.match(PLUS);
            Number multexprSyn = multexpr();
            if(inh instanceof Integer && multexprSyn instanceof  Integer)
                syn = arithexprprime(inh.intValue() + multexprSyn.intValue());
            else
                syn = arithexprprime(inh.doubleValue() + multexprSyn.doubleValue());
        } else if(token.match(MINUS)) {
            lex.match(MINUS);
            Number multexprSyn = multexpr();
            if(inh instanceof Integer && multexprSyn instanceof  Integer)
                syn = arithexprprime(inh.intValue() - multexprSyn.intValue());
            else
                syn = arithexprprime(inh.doubleValue() - multexprSyn.doubleValue());
        }
        return syn;
    }

    private Number multexpr() {
        Token token = lex.current();
        Number syn = null;
        if(token.match(OPEN_PARENTHESES) || token.match(ID) || token.match(INT_NUM) || token.match(REAL_NUM)) {
            Number simpleexprSyn = simpleexpr();
            syn = multexprprime(simpleexprSyn);
        }
        return syn;
    }

    private Number multexprprime(Number inh) {
        Token token = lex.current();
        Number syn = inh;
        if(token.match(MULTIPLY)) {
            lex.match(MULTIPLY);
            Number simpleexprSyn = simpleexpr();
            if(inh instanceof Integer && simpleexprSyn instanceof  Integer)
                syn = arithexprprime(inh.intValue() * simpleexprSyn.intValue());
            else
                syn = arithexprprime(inh.doubleValue() * simpleexprSyn.doubleValue());
        } else if(token.match(DIVIDE)) {
            lex.match(DIVIDE);
            Number simpleexprSyn = simpleexpr();
            if(inh instanceof Integer && simpleexprSyn instanceof Integer)
                syn = arithexprprime(inh.intValue() / simpleexprSyn.intValue());
            else
                syn = arithexprprime(inh.doubleValue() / simpleexprSyn.doubleValue());
        }
        return syn;
    }

    private Number simpleexpr() {
        Token token = lex.current();
        Number syn = null;
        if(token.match(OPEN_PARENTHESES)) {
            lex.match(OPEN_PARENTHESES);
            syn = arithexpr();
            lex.match(CLOSE_PARENTHESES);
        } else if(token.match(ID)) {
            syn = table.getValue(token.getLexeme());
            lex.next();
        } else if(token.match(INT_NUM)) {
            syn = token.getLexVal();
            lex.next();
        } else if(token.match(REAL_NUM)) {
            syn = token.getLexVal();
            lex.next();
        }
        return syn;
    }

    private void ifstmt(boolean exec) {
        Token token = lex.current();
        if(token.match(IF)) {
            lex.match(IF);
            lex.match(OPEN_PARENTHESES);
            Boolean boolsyn = boolexpr();
            lex.match(CLOSE_PARENTHESES);
            lex.match(THEN);
            stmt(exec && boolsyn != null && boolsyn);
            lex.match(ELSE);
            stmt(exec && boolsyn != null && !boolsyn);
        }
    }

    private Boolean boolexpr() {
        Token token = lex.current();
        Boolean syn = null;
        if(token.match(OPEN_PARENTHESES) || token.match(ID) || token.match(INT_NUM) || token.match(REAL_NUM)) {
            Number arithexprSyn = arithexpr();
            String op = boolop();
            syn = arithexpr(arithexprSyn, op);
        }
        return syn;
    }

    private String boolop() {
        Token token = lex.current();
        String syn = null;
        if(token.match(LESS_THAN) || token.match(LESS_THAN_OR_EQUAL) || token.match(GREATER_THAN) || token.match(GREATER_THAN_OR_EQUAL) || token.match(EQUAL)) {
            syn = token.getLexeme();
            lex.next();
        }
        return syn;
    }


}
