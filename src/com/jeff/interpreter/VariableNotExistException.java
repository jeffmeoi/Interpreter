package com.jeff.interpreter;

public class VariableNotExistException extends RuntimeException {

    private final String variableName;

    public VariableNotExistException(String name) {
        this.variableName = name;
    }
}
