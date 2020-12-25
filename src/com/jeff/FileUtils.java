package com.jeff;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileUtils {

    /**
     * 文件读入
     * @param filepath 需要读入的文件路径
     * @return 文件的所有内容
     * @throws IOException 文件io异常
     */
    public static String read(String filepath) throws IOException {
        InputStream is = new FileInputStream(filepath);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        is.close();
        return new String(bytes);
    }


    /**
     * 写回文件
     * @param filepath 需要写回的文件路径
     * @param string 待输出的字符串
     * @throws IOException 文件io异常
     */
    public static void write(String filepath, String string) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
        bufferedWriter.write(string);
        bufferedWriter.close();
    }

    public static NonTerminal readStartSymbolFromFile(String filepath, Map<NonTerminal, NonTerminal> nonTerminals) throws IOException {
        String symbol = FileUtils.read(filepath);
        Symbol startSymbol = new NonTerminal(symbol);
        return nonTerminals.get(startSymbol);
    }

    public static Map<Terminal, Terminal> readTerminalsFromFile(String filepath) throws IOException {
        Map<Terminal, Terminal> terminals = StringUtils.separateBySpace(FileUtils.read(filepath))
                .stream().map(s -> new Terminal(s))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        Terminal end = new Terminal(Terminal.END);
        terminals.put(end, end);
        return terminals;
    }

    public static Map<NonTerminal, NonTerminal> readNonTerminalsFromFile(String filepath) throws IOException {
        Map<NonTerminal, NonTerminal> nonTerminals = StringUtils.separateBySpace(FileUtils.read(filepath))
                .stream().map(s -> new NonTerminal(s))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        return nonTerminals;
    }

    public static List<ProductionRule> readProductionRulesFromFile(String filepath, Map<Terminal, Terminal> terminals,
                                                                   Map<NonTerminal, NonTerminal> nonTerminals) throws IOException {
        return ProductionRule.getProductionRulesByStringList(StringUtils.separateByLine(FileUtils.read(filepath)), terminals, nonTerminals);
    }
}
