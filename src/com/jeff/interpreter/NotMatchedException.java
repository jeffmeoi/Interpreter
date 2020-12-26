package com.jeff.interpreter;

public class NotMatchedException extends RuntimeException {
    private final Token token;
    private final String tokenStr;
    public NotMatchedException(Token token, String tokenStr) {
        this.token = token;
        this.tokenStr = tokenStr;
    }

    @Override
    public String toString() {
        return token + " and " + tokenStr + " is not matched";
    }
}
