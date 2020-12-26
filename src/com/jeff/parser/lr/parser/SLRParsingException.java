package com.jeff.parser.lr.parser;

public class SLRParsingException extends RuntimeException {

    private final String msg;
    public SLRParsingException(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }

}
