package com.jeff.interpreter;

import com.jeff.FileUtils;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String context = FileUtils.readAll("interpret.in");
        InterpreterEx2Grammar interpreter = new InterpreterEx2Grammar();
        // 开始解释
        try {
            interpreter.interpret(context);
            String output = interpreter.toString();
            System.out.println(output);
            FileUtils.writeAll("interpret.out", output);
        } catch (NotMatchedException | VariableNotExistException | TypeCheckException | InterpretException | ExpressionException e) {
            System.out.println(e);
        }
    }

}
