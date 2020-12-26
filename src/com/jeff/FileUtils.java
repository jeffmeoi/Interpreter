package com.jeff;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Terminal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileUtils {

    public static String readAll(String filepath) throws IOException {
        InputStream is = new FileInputStream(filepath);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        is.close();
        return new String(bytes);
    }


    public static void writeAll(String filepath, String string) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
        bufferedWriter.write(string);
        bufferedWriter.close();
    }

    public static NonTerminal readStartSymbolFromFile(String filepath) throws IOException {
        String symbol = FileUtils.readAll(filepath);
        return new NonTerminal(symbol);
    }

    public static Map<Terminal, Terminal> readTerminalsFromFile(String filepath) throws IOException {
        Map<Terminal, Terminal> terminals = StringUtils.separateBySpace(FileUtils.readAll(filepath))
                .stream().map(Terminal::new)
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        Terminal end = new Terminal(Terminal.END);
        terminals.put(end, end);
        return terminals;
    }

    public static Map<NonTerminal, NonTerminal> readNonTerminalsFromFile(String filepath) throws IOException {
        return StringUtils.separateBySpace(FileUtils.readAll(filepath))
                .stream().map(NonTerminal::new)
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
    }

    public static List<ProductionRule> readProductionRulesFromFile(String filepath, Map<Terminal, Terminal> terminals,
                                                                   Map<NonTerminal, NonTerminal> nonTerminals) throws IOException {
        List<ProductionRule> rules = new ArrayList<>();
        List<String> lines = StringUtils.separateByLine(FileUtils.readAll(filepath));
        for(String line : lines)
            ProductionRule.generateProductionRule(line, terminals, nonTerminals).ifPresent(rules::add);
        return rules;
    }
}
