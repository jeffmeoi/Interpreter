package com.jeff.parser.lr;

import com.jeff.StringUtils;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Terminal;
import com.jeff.parser.lr.parser.LRParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

//        LRParser parser = new LRParser();
//        String context = "{ ID = NUM ; }";

        LRParser parser = new LRParser("start-symbol", "terminals", "non-terminals", "rules");
        String context = "int ID = INTNUM ; int ID = INTNUM ; real ID = REALNUM ; { }";

        // 将context分成Terminal列表
        List<Terminal> tokens = StringUtils.separateBySpace(context).stream().map(Terminal::new).collect(Collectors.toList());
        // 使用LR Parser解析tokens，并返回输出产生式列表
        LinkedList<ProductionRule> ruleStack = parser.parse(tokens);
        // 输出right-most derivation的推导过程
        System.out.println(parser.generateDerivationString(ruleStack));
    }
}
