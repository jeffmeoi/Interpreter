package com.jeff.interpreter;

/**
 * Token实体对象
 */
public class Token {

    private final TokenType tokenType;
    private final String lexeme;
    private final Number lexval;

    public Token(TokenType tokenType, String lexeme, Number lexval) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.lexval = lexval;
    }

    // 输入的token 匹配产生式中的 lexeme
    public boolean match4Ex4Grammar(String lexeme){
        // 如果lexeme是INT_NUM，由于token的lexeme为真实值，因此需要匹配TokenType是否为INT_NUM
        if(lexeme.equals(InterpreterEx4Grammar.INT_NUM) && this.tokenType == TokenType.INT_NUM)
            return true;
        // 如果lexeme是REAL_NUM，由于token的lexeme为真实值，因此需要匹配TokenType是否为REAL_NUM
        else if(lexeme.equals(InterpreterEx4Grammar.REAL_NUM) && this.tokenType == TokenType.REAL_NUM)
            return true;
        // 如果lexeme是ID类型，由于token的lexeme为标识符具体名字，因此需要匹配TokenType是否为IDENTIFIER
        else if(lexeme.equals(InterpreterEx4Grammar.ID) && this.tokenType == TokenType.IDENTIFIER)
            return true;
        else    // 如果既不是INT_NUM又不是REAL_NUM又不是ID，则只需要比较token的lexeme和需要匹配的产生式的lexeme
            return lexeme.equals(this.lexeme);
    }

    // 输入的token 匹配产生式中的 lexeme
    // ex2的文法
    public boolean match4Ex2Grammar(String lexeme){
        // 如果lexeme是INT_NUM，由于token的lexeme为真实值，因此需要匹配TokenType是否为INT_NUM
        if(lexeme.equals(InterpreterEx2Grammar.NUM) && (this.tokenType == TokenType.INT_NUM || this.tokenType == TokenType.REAL_NUM))
            return true;
            // 如果lexeme是ID类型，由于token的lexeme为标识符具体名字，因此需要匹配TokenType是否为IDENTIFIER
        else if(lexeme.equals(InterpreterEx2Grammar.ID) && this.tokenType == TokenType.IDENTIFIER)
            return true;
        else    // 如果既不是INT_NUM又不是REAL_NUM又不是ID，则只需要比较token的lexeme和需要匹配的产生式的lexeme
            return lexeme.equals(this.lexeme);
    }

    public Number getLexval() {
        return lexval;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + tokenType + '\'' +
                ", lexeme='" + lexeme + '\'' +
                ", lexval=" + lexval +
                '}';
    }
}
