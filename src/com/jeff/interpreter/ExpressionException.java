package com.jeff.interpreter;

public class ExpressionException extends RuntimeException {
    @Override
    public String toString() {
        return "Expression compute error";
    }
}
