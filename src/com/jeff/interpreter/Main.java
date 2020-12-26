package com.jeff.interpreter;

public class Main {

    public static void main(String[] args) {
        String context = "int a = 1 ; int b = 2 ;  real c = 3.0 ; \n" +
                "{ \n" +
                "a = a + 1 ; \n" +
                "b = a * b ; \n" +
                "if ( a < b ) \n" +
                "then \n" +
                "   c = c / 2 ; \n" +
                "else \n" +
                "   c = c / 4 ; \n" +
                "} \n";
        Interpreter interpreter = new Interpreter();
        // 开始解释
        try {
            interpreter.interpret(context);
        } catch (NotMatchedException | VariableNotExistException | TypeCheckException e) {
            System.out.println(e);
        }
        // 如果解释成功，则打印全局符号表
        if(interpreter.isSuccess())
            interpreter.print();
    }

}
