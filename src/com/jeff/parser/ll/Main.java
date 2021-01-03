package com.jeff.parser.ll;

import com.jeff.FileUtils;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // 需要分析的字符串
        String context = FileUtils.readAll("llparser.in");
        // 初始化解释器
        LLParser parser = new LLParser();
        // 解释器匹配，判断字符串是否符合syntax
        if(parser.parse(context)){
            // 若匹配，则输出解释树，tab间距为2
            String output = parser.outputParseTree(2);
            System.out.print(output);
            FileUtils.writeAll("llparser.out", output);
        } else {
            // 若不匹配，则输出false
            System.out.println(false);
            FileUtils.writeAll("llparser.out", "false");
        }
    }
}
