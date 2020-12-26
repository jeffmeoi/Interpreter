package com.jeff.parser.lr;

import com.jeff.parser.ProductionRule;
import com.jeff.parser.Terminal;
import com.jeff.parser.lr.parser.LRParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        LRParser parser = new LRParser();
        String context = "{ ID = NUM ; }";

//        LRParser parser = new LRParser("start-symbol", "terminals", "non-terminals", "rules");
//        String context = "int ID = INTNUM ; int ID = INTNUM ; real ID = REALNUM ; { }";

        List<Terminal> tokens = Arrays.stream(context.split("\\s+")).map(Terminal::new).collect(Collectors.toList());
        LinkedList<ProductionRule> ruleStack = parser.parse(tokens);
        System.out.println(parser.generateDerivationString(ruleStack));
    }
}
