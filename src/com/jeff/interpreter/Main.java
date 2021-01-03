package com.jeff.interpreter;

public class Main {

    public static void main(String[] args) {
        String context = "{\n" +
                "    a = 0 ; \n" +
                "    c = 1 ;\n" +
                "    if ( a > 0 ) then \n" +
                "        b = 1 ;\n" +
                "    else\n" +
                "        c = 4 ;\n" +
                "    b = 0 ;\n" +
                "    a = 5 ;\n" +
                "    c = 256 ;\n" +
                "    while ( a > 0 ) {\n" +
                "        b = b + 1 ;\n" +
                "        c = c / 2 ;\n" +
                "        a = a - 1 ;\n" +
                "    }\n" +
                "}";
        InterpreterEx2Grammar interpreter = new InterpreterEx2Grammar();
        // 开始解释
        try {
            interpreter.interpret(context);
            interpreter.print();
        } catch (NotMatchedException | VariableNotExistException | TypeCheckException | InterpretException | ExpressionException e) {
            System.out.println(e);
        }
    }

}
