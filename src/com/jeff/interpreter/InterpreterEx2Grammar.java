package com.jeff.interpreter;

/**
 * 解释器
 */
public class InterpreterEx2Grammar {

    // 终结符字符串常量
    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";
    public static final String IF = "if";
    public static final String WHILE = "while";
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
    public static final String NUM = "NUM";
    public static final String END = "$";

    // 词法分析器
    private Lex lex;
    // 由于该语法只需考虑一个作用域，因此只需要全局作用域的变量符号表
    private final VariableTable variableTable = new VariableTable();

    public InterpreterEx2Grammar() {

    }

    // 开始解释程序
    public void interpret(String context){
        this.lex = new Lex(context);
        program(true);                      // 开始执行解释
        if(!lex.current().match4Ex2Grammar(END))
            throw new InterpretException(lex.current());
    }


    @Override
    public String toString() {
        return variableTable.toString();
    }

    /**
     * 解释程序入口
     * @param exec 内部的赋值是否需要执行
     */
    public void program(boolean exec){
        Token token = lex.current();
        // 产生式1的firstSet为{, int, real
        if(token.match4Ex2Grammar(OPEN_BRACE)) {
            compoundstmt(exec);
        }
    }
    public void compoundstmt(boolean exec){
        Token token = lex.current();
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(OPEN_BRACE)) {
            lex.match2(OPEN_BRACE);
            stmts(exec);
            lex.match2(CLOSE_BRACE);
        }
    }

    private void stmts(boolean exec) {
        Token token = lex.current();
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(OPEN_BRACE) || token.match4Ex2Grammar(IF) || token.match4Ex2Grammar(ID) || token.match4Ex2Grammar(WHILE)) {
            stmt(exec);
            stmts(exec);
        }
    }

    private void stmt(boolean exec) {
        Token token = lex.current();
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(OPEN_BRACE)) {
            compoundstmt(exec);
        } else if(token.match4Ex2Grammar(IF)) {
            ifstmt(exec);
        } else if(token.match4Ex2Grammar(ID)) {
            assgstmt(exec);
        } else if(token.match4Ex2Grammar(WHILE)) {
            whilestmt(exec);
        }
    }

    private void whilestmt(boolean exec) {
        Token token = lex.current();
        if(token.match4Ex2Grammar(WHILE)) {
            boolean condition;
            do {
                int currPos = lex.getCurrPos();
                lex.match2(WHILE);
                lex.match2(OPEN_PARENTHESES);
                Boolean boolsyn = boolexpr();
                condition = exec && boolsyn != null && boolsyn;
                lex.match2(CLOSE_PARENTHESES);
                stmt(condition);
                if(!condition)
                    break;
                lex.setCurrPos(currPos);
            } while (true);
        }
    }

    private void assgstmt(boolean exec) {
        Token token = lex.current();
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(ID)) {
            String id = lex.match2(ID).getLexeme();
            lex.match2(IS);
            Number syn = arithexpr();       // id.val = arithexpr.syn
            lex.match2(SEMICOLON);
            if(exec) {                      // 如果exec为false，则处于boolexpr为false的分支，赋值操作不执行
                try {
                    if (variableTable.getValue(id) instanceof Integer && syn instanceof Double)      // 不允许将real类型的表达式值赋给int类型
                        throw new TypeCheckException(id, "int", syn, "real");
                    else if (variableTable.getValue(id) instanceof Double && syn instanceof Integer)      // 不允许将real类型的表达式值赋给int类型
                        variableTable.put(id, (double) syn.intValue());
                    else
                        variableTable.put(id, syn);
                }   catch (VariableNotExistException e) {
                    variableTable.put(id, syn);
                }
            }
        }
    }

    private Number arithexpr() {
        Token token = lex.current();
        Number syn = null;
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(OPEN_PARENTHESES) || token.match4Ex2Grammar(ID) || token.match4Ex2Grammar(NUM)) {
            Number multexprSyn = multexpr();
            syn = arithexprprime(multexprSyn);              // arithexprprime.inh = multexpr.syn
        }
        return syn;
    }

    /**
     *
     * @param inh boolop左侧的数值
     * @param op boolop的运算符类型
     * @return boolexpr的值，true/false
     */
    private Boolean arithexpr(Number inh, String op) {
        Number syn = arithexpr();              // 先计算出boolop右侧的值
        switch (op) {                          // 根据不同的运算符类型执行不同的bool操作
            case LESS_THAN:
                if(inh instanceof Integer && syn instanceof Integer)    // 自动类型转换，只有左值和右值均为int类型时，才按照int类型比较
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

    /**
     * @param inh arithexprprime左侧的multexpr的综合属性值
     * @return 含arithexprprime左侧的multexpr在内的综合属性值
     */
    private Number arithexprprime(Number inh) {
        Token token = lex.current();
        Number syn = inh;       // 当arithexprprime为epsilon的时候，即只存在左操作数，不存在运算符和右操作数的时候，运算的结果应该是左操作数的值
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(PLUS)) {
            lex.match2(PLUS);
            Number multexprSyn = multexpr();
            if(multexprSyn == null)
                throw new ExpressionException();
            if(inh instanceof Integer && multexprSyn instanceof  Integer)       // arithexprprime1.inh = arithexprprime.inh + multexpr.syn
                syn = arithexprprime(inh.intValue() + multexprSyn.intValue());  // arithexprprime.syn = arithexprprime1.syn
            else
                syn = arithexprprime(inh.doubleValue() + multexprSyn.doubleValue());
        } else if(token.match4Ex2Grammar(MINUS)) {
            lex.match2(MINUS);
            Number multexprSyn = multexpr();
            if(inh instanceof Integer && multexprSyn instanceof  Integer)       // arithexprprime1.inh = arithexprprime.inh - multexpr.syn
                syn = arithexprprime(inh.intValue() - multexprSyn.intValue());  // arithexprprime.syn = arithexprprime1.syn
            else
                syn = arithexprprime(inh.doubleValue() - multexprSyn.doubleValue());
        }
        return syn;
    }

    private Number multexpr() {
        Token token = lex.current();
        Number syn = null;
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(OPEN_PARENTHESES) || token.match4Ex2Grammar(ID) || token.match4Ex2Grammar(NUM)) {
            Number simpleexprSyn = simpleexpr();         // multexprprime.inh = simpleexpr.syn
            syn = multexprprime(simpleexprSyn);         // multexpr.syn = multexprprime.syn
        }
        return syn;
    }

    private Number multexprprime(Number inh) {
        Token token = lex.current();
        Number syn = inh;
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(MULTIPLY)) {
            lex.match2(MULTIPLY);
            Number simpleexprSyn = simpleexpr();
            if(simpleexprSyn == null)
                throw new ExpressionException();
            if(inh instanceof Integer && simpleexprSyn instanceof  Integer)             // arithexprprime1.inh = multexprprime.inh * simpleexpr.syn
                syn = arithexprprime(inh.intValue() * simpleexprSyn.intValue());    // multexprprime.syn = arithexprprime1.syn
            else
                syn = arithexprprime(inh.doubleValue() * simpleexprSyn.doubleValue());
        } else if(token.match4Ex2Grammar(DIVIDE)) {
            lex.match2(DIVIDE);
            Number simpleexprSyn = simpleexpr();
            if(inh instanceof Integer && simpleexprSyn instanceof Integer)             // arithexprprime1.inh = multexprprime.inh / simpleexpr.syn
                syn = arithexprprime(inh.intValue() / simpleexprSyn.intValue());    // multexprprime.syn = arithexprprime1.syn
            else
                syn = arithexprprime(inh.doubleValue() / simpleexprSyn.doubleValue());
        }
        return syn;
    }

    private Number simpleexpr() {
        Token token = lex.current();
        Number syn = null;
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(OPEN_PARENTHESES)) {
            lex.match2(OPEN_PARENTHESES);
            syn = arithexpr();              // simpleexpr.syn = arithexpr.syn
            lex.match2(CLOSE_PARENTHESES);
        } else if(token.match4Ex2Grammar(ID)) {
            syn = variableTable.getValue(token.getLexeme());        // simpleexpr.syn = ID.lexeme
            lex.next();
        } else if(token.match4Ex2Grammar(NUM)) {
            syn = token.getLexval();                        // simpleexpr.syn = REAL_NUM.lexval
            lex.next();
        }
        return syn;
    }

    private void ifstmt(boolean exec) {
        Token token = lex.current();
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(IF)) {
            lex.match2(IF);
            lex.match2(OPEN_PARENTHESES);
            Boolean boolsyn = boolexpr();
            lex.match2(CLOSE_PARENTHESES);
            lex.match2(THEN);
            stmt(exec && boolsyn != null && boolsyn);                         // 如果ifstmt处于被执行的逻辑，并且boolexpr.syn为true，则stmt能够被执行
            lex.match2(ELSE);
            stmt(exec && boolsyn != null && !boolsyn);                         // 如果ifstmt处于被执行的逻辑，并且boolexpr.syn为false，则stmt能够被执行
        }
    }

    private Boolean boolexpr() {
        Token token = lex.current();
        Boolean syn = null;
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(OPEN_PARENTHESES) || token.match4Ex2Grammar(ID) || token.match4Ex2Grammar(NUM)) {
            Number arithexprSyn = arithexpr();                                                      // arithexpr1.inh = arithexpr.syn
            String op = boolop();                                                                   // arithexpr1.op = boolop.op
            syn = arithexpr(arithexprSyn, op);                                                    // boolexpr.syn = arithexpr1.syn
        }
        return syn;
    }

    private String boolop() {
        Token token = lex.current();
        String syn = null;
        // match4Ex2Grammar the first set
        if(token.match4Ex2Grammar(LESS_THAN) || token.match4Ex2Grammar(LESS_THAN_OR_EQUAL) || token.match4Ex2Grammar(GREATER_THAN) || token.match4Ex2Grammar(GREATER_THAN_OR_EQUAL) || token.match4Ex2Grammar(EQUAL)) {
            syn = token.getLexeme();            // boolop.op = token.lexeme
            lex.next();
        }
        return syn;
    }


}
