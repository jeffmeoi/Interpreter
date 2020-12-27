package com.jeff.interpreter;

public class InterpreterException extends RuntimeException {
    private Token current;
    public InterpreterException(Token current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "interpret is finished but " + current + " is just inputted.";
    }
}
