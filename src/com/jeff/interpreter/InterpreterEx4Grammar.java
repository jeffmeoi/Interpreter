package com.jeff.interpreter;

/**
 * 解释器
 */
public class InterpreterEx4Grammar {

    // 终结符字符串常量
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

    // 词法分析器
    private Lex lex;
    // 解释器需要解释的文本
    private String context;
    // 由于该语法只需考虑一个作用域，因此只需要全局作用域的变量符号表
    private VariableTable variableTable = new VariableTable();

    public InterpreterEx4Grammar() {

    }

    // 开始解释程序
    public void interpret(String context){
        this.context = context;
        this.lex = new Lex(context);
        program(true);                      // 开始执行解释
        if(!lex.current().match(END))
            throw new InterpreterException(lex.current());
    }



    // 打印解释完成后的符号表
    public void print() {
        System.out.println(variableTable);
    }

    /**
     * 解释程序入口
     * @param exec 内部的赋值是否需要执行
     */
    public void program(boolean exec){
        Token token = lex.current();
        // 产生式1的firstSet为{, int, real
        if(token.match(OPEN_BRACE) || token.match(INT) || token.match(REAL)) {
            decls();
            compoundstmt(exec);
        }
    }


    private void decls() {
        Token token = lex.current();
        // match the first set
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
        // match the first set
        if(token.match(INT)) {
            lex.match(INT);
            id = lex.match(ID).getLexeme();         // 获取ID的lexeme，即标识符的名字
            lex.match(IS);
            syn = lex.match(INT_NUM).getLexval();
            variableTable.put(id, syn);         // 插入符号表
        } else if(token.match(REAL)) {
            lex.match(REAL);
            id = lex.match(ID).getLexeme();         // 获取ID的lexeme，即标识符的名字
            lex.match(IS);
            syn = lex.match(REAL_NUM).getLexval();
            variableTable.put(id, syn);         // 插入符号表
        }
    }

    public void compoundstmt(boolean exec){
        Token token = lex.current();
        // match the first set
        if(token.match(OPEN_BRACE)) {
            lex.match(OPEN_BRACE);
            stmts(exec);
            lex.match(CLOSE_BRACE);
        }
    }

    private void stmts(boolean exec) {
        Token token = lex.current();
        // match the first set
        if(token.match(OPEN_BRACE) || token.match(IF) || token.match(ID)) {
            stmt(exec);
            stmts(exec);
        }
    }

    private void stmt(boolean exec) {
        Token token = lex.current();
        // match the first set
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
        // match the first set
        if(token.match(ID)) {
            String id = lex.match(ID).getLexeme();
            lex.match(IS);
            Number syn = arithexpr();       // id.val = arithexpr.syn
            lex.match(SEMICOLON);
            if(exec) {                      // 如果exec为false，则处于boolexpr为false的分支，赋值操作不执行
                if(variableTable.getValue(id) instanceof Integer && syn instanceof Double)      // 不允许将real类型的表达式值赋给int类型
                    throw new TypeCheckException(id, "int", syn, "real");
                if(variableTable.getValue(id) instanceof Double && syn instanceof Integer)      // 不允许将real类型的表达式值赋给int类型
                    variableTable.setValue(id, (double) syn.intValue());
                variableTable.setValue(id, syn);
            }
        }
    }

    private Number arithexpr() {
        Token token = lex.current();
        Number syn = null;
        // match the first set
        if(token.match(OPEN_PARENTHESES) || token.match(ID) || token.match(INT_NUM) || token.match(REAL_NUM)) {
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
     * @param inh arithexprprime左侧的multiexpr的综合属性值
     * @return 含arithexprprime左侧的multiexpr在内的综合属性值
     */
    private Number arithexprprime(Number inh) {
        Token token = lex.current();
        Number syn = inh;       // 当arithexprprime为epsilon的时候，即只存在左操作数，不存在运算符和右操作数的时候，运算的结果应该是左操作数的值
        // match the first set
        if(token.match(PLUS)) {
            lex.match(PLUS);
            Number multexprSyn = multexpr();
            if(multexprSyn == null)
                throw new ExpressionException();
            if(inh instanceof Integer && multexprSyn instanceof  Integer)       // arithexprprime1.inh = arithexprprime.inh + multexpr.syn
                syn = arithexprprime(inh.intValue() + multexprSyn.intValue());  // arithexprprime.syn = arithexprprime1.syn
            else
                syn = arithexprprime(inh.doubleValue() + multexprSyn.doubleValue());
        } else if(token.match(MINUS)) {
            lex.match(MINUS);
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
        // match the first set
        if(token.match(OPEN_PARENTHESES) || token.match(ID) || token.match(INT_NUM) || token.match(REAL_NUM)) {
            Number simpleexprSyn = simpleexpr();         // multexprprime.inh = simpleexpr.syn
            syn = multexprprime(simpleexprSyn);         // multexpr.syn = multexprprime.syn
        }
        return syn;
    }

    private Number multexprprime(Number inh) {
        Token token = lex.current();
        Number syn = inh;
        // match the first set
        if(token.match(MULTIPLY)) {
            lex.match(MULTIPLY);
            Number simpleexprSyn = simpleexpr();
            if(simpleexprSyn == null)
                throw new ExpressionException();
            if(inh instanceof Integer && simpleexprSyn instanceof  Integer)             // arithexprprime1.inh = multexprprime.inh * simpleexpr.syn
                syn = arithexprprime(inh.intValue() * simpleexprSyn.intValue());    // multexprprime.syn = arithexprprime1.syn
            else
                syn = arithexprprime(inh.doubleValue() * simpleexprSyn.doubleValue());
        } else if(token.match(DIVIDE)) {
            lex.match(DIVIDE);
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
        // match the first set
        if(token.match(OPEN_PARENTHESES)) {
            lex.match(OPEN_PARENTHESES);
            syn = arithexpr();              // simpleexpr.syn = arithexpr.syn
            lex.match(CLOSE_PARENTHESES);
        } else if(token.match(ID)) {
            syn = variableTable.getValue(token.getLexeme());        // simpleexpr.syn = ID.lexeme
            lex.next();
        } else if(token.match(INT_NUM)) {
            syn = token.getLexval();                        // simpleexpr.syn = INT_NUM.lexval
            lex.next();
        } else if(token.match(REAL_NUM)) {
            syn = token.getLexval();                        // simpleexpr.syn = REAL_NUM.lexval
            lex.next();
        }
        return syn;
    }

    private void ifstmt(boolean exec) {
        Token token = lex.current();
        // match the first set
        if(token.match(IF)) {
            lex.match(IF);
            lex.match(OPEN_PARENTHESES);
            Boolean boolsyn = boolexpr();
            lex.match(CLOSE_PARENTHESES);
            lex.match(THEN);
            stmt(exec && boolsyn != null && boolsyn);                         // 如果ifstmt处于被执行的逻辑，并且boolexpr.syn为true，则stmt能够被执行
            lex.match(ELSE);
            stmt(exec && boolsyn != null && !boolsyn);                         // 如果ifstmt处于被执行的逻辑，并且boolexpr.syn为false，则stmt能够被执行
        }
    }

    private Boolean boolexpr() {
        Token token = lex.current();
        Boolean syn = null;
        // match the first set
        if(token.match(OPEN_PARENTHESES) || token.match(ID) || token.match(INT_NUM) || token.match(REAL_NUM)) {
            Number arithexprSyn = arithexpr();                                                      // arithexpr1.inh = arithexpr.syn
            String op = boolop();                                                                   // arithexpr1.op = boolop.op
            syn = arithexpr(arithexprSyn, op);                                                    // boolexpr.syn = arithexpr1.syn
        }
        return syn;
    }

    private String boolop() {
        Token token = lex.current();
        String syn = null;
        // match the first set
        if(token.match(LESS_THAN) || token.match(LESS_THAN_OR_EQUAL) || token.match(GREATER_THAN) || token.match(GREATER_THAN_OR_EQUAL) || token.match(EQUAL)) {
            syn = token.getLexeme();            // boolop.op = token.lexeme
            lex.next();
        }
        return syn;
    }


}
