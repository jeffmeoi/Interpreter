package com.jeff.interpreter;

public class TypeCheckException extends RuntimeException {
    private final String id;
    private final String idType;
    private final Number syn;
    private final String synType;
    public TypeCheckException(String id, String idType, Number syn, String synType) {
        this.id = id;
        this.idType = idType;
        this.syn = syn;
        this.synType = synType;
    }

    @Override
    public String toString() {
        return "The type of Identifier " + id + " is " + idType + ", but the type of expression value " + syn + " is " + synType + ".";
    }
}
