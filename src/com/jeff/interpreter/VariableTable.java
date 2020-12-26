package com.jeff.interpreter;

import java.util.HashMap;
import java.util.Map;

public class VariableTable {

    static class Variable {
        String name;
        Number val;

        @Override
        public String toString() {
            return "Variable:" + name + "=" + val;
        }
    }

    private final Map<String, Variable> map = new HashMap<>();

    public void put(Token token){
        Variable variable = new Variable();
        variable.name = token.getLexeme();
        variable.val = token.getLexVal();
        map.put(variable.name, variable);
    }

    public Number getValue(String name){
        Variable variable = map.get(name);
        if(variable == null)
            throw new VariableNotExistException();
        return variable.val;
    }

    public void setValue(String name, Number val) {
        Variable variable = map.get(name);
        if(variable == null)
            throw new VariableNotExistException();
        variable.val = val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Variable variable : map.values()) {
            sb.append(variable.name).append(":").append(variable.val).append("\n");
        }
        return sb.toString();
    }
}
