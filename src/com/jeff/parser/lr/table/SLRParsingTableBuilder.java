package com.jeff.parser.lr.table;

import com.jeff.FileUtils;
import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.io.IOException;
import java.util.*;

public class SLRParsingTableBuilder {

    private final Symbol startSymbol;
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;
    private final List<ProductionRule> rules;

    public SLRParsingTableBuilder(Symbol startSymbol, Map<Terminal, Terminal> terminals, Map<NonTerminal, NonTerminal> nonTerminals, List<ProductionRule> rules) {
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;
    }

    public SLRParsingTableBuilder(String startSymbolFilepath, String terminalFilepath, String nonTerminalFilepath, String ruleFilepath) throws IOException {
        this.terminals = FileUtils.readTerminalsFromFile(terminalFilepath);
        this.nonTerminals = FileUtils.readNonTerminalsFromFile(nonTerminalFilepath);
        this.startSymbol = FileUtils.readStartSymbolFromFile(startSymbolFilepath);
        this.rules = FileUtils.readProductionRulesFromFile(ruleFilepath, terminals, nonTerminals);
    }

    public SLRParsingTable build() {
        Processor processor = new Processor(startSymbol, terminals, nonTerminals, rules);
        HashMap<Symbol, Set<Symbol>> firstSets = processor.generateAllFirstSet();     // 计算所有符号的first set
        HashMap<Symbol, Set<Symbol>> followSets = processor.generateAllFollowSet();   // 计算所有非终结符的follow set
//        输出first set 和 follow set
//        System.out.println("first set");
//        for(Map.Entry<Symbol, Set<Symbol>> entry : firstSets.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
//        System.out.println("follow set");
//        for(Map.Entry<Symbol, Set<Symbol>> entry : followSets.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }

        // Closure I0的初始化Item，即 program -> .compoundstmt
        Item initialItem = new Item(0, 0, rules.get(0));
        Closure I0 = new Closure(new HashSet<>(Collections.singletonList(initialItem)), rules); // 传入初始的Closure的item集合，根据产生式集合生成完整的I0项目集闭包
        CanonicalCollection collection = new CanonicalCollection(I0, terminals, nonTerminals);          // 根据项目集闭包I0，生成项目集规范族
        ActionTable actionTable = new ActionTable(collection, startSymbol, followSets);     // 根据项目集规范族生成action表
        GotoTable gotoTable = new GotoTable(collection);                                    // 根据项目集规范族生成goto表

        return new SLRParsingTable(actionTable, gotoTable);                                 // 返回SLR Parsing Table，由action表和goto表组成
    }


    public static void main(String[] args) throws IOException {
        // 根据四个输入文件生成
        SLRParsingTableBuilder tableParser = new SLRParsingTableBuilder("start-symbol", "terminals", "non-terminals", "rules");
        SLRParsingTable table = tableParser.build();
        System.out.println(table);
    }

}
