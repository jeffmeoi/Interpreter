package com.jeff.interpreter;

public class InterpretException extends RuntimeException {
    private final Token current;
    public InterpretException(Token current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "interpret is finished but " + current + " is just inputted.";
    }
}
