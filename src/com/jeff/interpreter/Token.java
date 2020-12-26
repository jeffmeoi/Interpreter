package com.jeff.interpreter;

/**
 * Token实体对象
 */
public class Token {

    private final TokenType tokenType;
    private final String lexeme;
    private final Number lexVal;

    public Token(TokenType tokenType, String lexeme, Number lexVal) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.lexVal = lexVal;
    }

    public boolean match(String lexeme){
        if(lexeme.equals(Interpreter.INT_NUM) && this.tokenType == TokenType.INT_NUM)
            return true;
        else if(lexeme.equals(Interpreter.REAL_NUM) && this.tokenType == TokenType.REAL_NUM)
            return true;
        else if(lexeme.equals(Interpreter.ID) && this.tokenType == TokenType.IDENTIFIER)
            return true;
        else
            return lexeme.equals(this.lexeme);
    }

    public Number getLexVal() {
        return lexVal;
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
                ", lexVal=" + lexVal +
                '}';
    }
}
