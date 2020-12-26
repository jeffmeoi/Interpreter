package com.jeff.lex;

import com.jeff.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // 读入文件的所有行
        String context = FileUtils.readAll("test1.toy");
        // 初始化tokenizer
        Tokenizer tokenizer = new Tokenizer(context);
        // 读入所有token
        List<Token> tokenList = new ArrayList<>();
        while(tokenizer.hasNext()) {
            Token token = tokenizer.next();
            if(token != null) {
                tokenList.add(token);
            }
        }
        StringBuilder sb = new StringBuilder();
        // 判断token的类型并set
        for(Token token : tokenList) {
            token.setTokenType(LexicalAnalyzer.getTokenType(token.getAttributeValue()));
            sb.append(token).append(System.lineSeparator());
        }
        // 写回文件
        FileUtils.writeAll("test1.tok", sb.toString());
    }
}
