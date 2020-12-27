package com.jeff.interpreter;

import java.util.HashMap;
import java.util.Map;

public class VariableTable {

    /**
     * 变量表内的变量类
     */
    static class Variable {
        String name;
        Number val;

        @Override
        public String toString() {
            return "Variable:" + name + "=" + val;
        }
    }

    private final Map<String, Variable> map = new HashMap<>();

    /**
     * 存入新的变量，变量名为token的lexeme，变量值为token的lexval
     * @param name 变量名
     * @param val 变量值
     */
    public void put(String name, Number val){
        Variable variable = new Variable();
        variable.name = name;
        variable.val = val;
        map.put(variable.name, variable);
    }

    /**
     * 获取变量的值
     * @param name 变量标识符
     * @return 变量值
     */
    public Number getValue(String name){
        Variable variable = map.get(name);
        if(variable == null)
            throw new VariableNotExistException(name);
        return variable.val;
    }

    /**
     * 设置变量的值
     * @param name 变量标识符
     * @param val 变量值
     */
    public void setValue(String name, Number val) {
        Variable variable = map.get(name);
        if(variable == null)
            throw new VariableNotExistException(name);
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
