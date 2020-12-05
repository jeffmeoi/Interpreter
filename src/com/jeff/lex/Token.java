package com.jeff.lex;

/**
 * Token实体对象
 */
public class Token {

    private String tokenType;
    private String attributeValue;
    private int lineNumber;
    private int linePosition;

    public Token(String tokenType, String attributeValue, int lineNumber, int linePosition) {
        this.tokenType = tokenType;
        this.attributeValue = attributeValue;
        this.lineNumber = lineNumber;
        this.linePosition = linePosition;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLinePosition() {
        return linePosition;
    }

    public void setLinePosition(int linePosition) {
        this.linePosition = linePosition;
    }

    @Override
    public String toString() {
        return "(" + tokenType + "," + attributeValue + "," + lineNumber + "," + linePosition + ")";
    }
}
