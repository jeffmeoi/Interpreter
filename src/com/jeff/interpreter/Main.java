package com.jeff.interpreter;

public class Main {

    public static void main(String[] args) {
        String context = "int a = 1 ; int b = 2 ;  real c = 3.0 ;\n" +
                "int d = 2 ;\n" +
                "    {\n" +
                "      c = 1 ;\n" +
                "        if ( a > 0 ) then b = 1 ;\n" +
                "        else c = 4.0 ;\n" +
                "      }\n" +
                "\n";
        Interpreter interpreter = new Interpreter();
        // 开始解释
        try {
            interpreter.interpret(context);
            interpreter.print();
        } catch (NotMatchedException | VariableNotExistException | TypeCheckException | InterpreterException | ExpressionException e) {
            System.out.println(e);
        }
    }

}
