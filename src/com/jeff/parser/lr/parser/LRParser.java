package com.jeff.parser.lr.parser;

import com.jeff.FileUtils;
import com.jeff.parser.*;
import com.jeff.parser.lr.table.Action;
import com.jeff.parser.lr.table.SLRParsingTable;
import com.jeff.parser.lr.table.SLRParsingTableBuilder;

import java.io.IOException;
import java.util.*;

public class LRParser {

    private final Symbol startSymbol;
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;
    private final List<ProductionRule> rules;
    private final SLRParsingTable table;

    public LRParser() {
        this.startSymbol = LRConstant.startSymbol;
        this.terminals = LRConstant.terminals;
        this.nonTerminals = LRConstant.nonTerminals;
        this.rules = LRConstant.rules;
        this.table = new SLRParsingTableBuilder(startSymbol, terminals, nonTerminals, rules).build();     // build SLR Parsing Table
//        System.out.println(table);
    }

    public LRParser(String startSymbolFilepath, String terminalFilepath, String nonTerminalFilepath, String ruleFilepath) throws IOException {
        // 从文件中读取四个参数
        this.terminals = FileUtils.readTerminalsFromFile(terminalFilepath);
        this.nonTerminals = FileUtils.readNonTerminalsFromFile(nonTerminalFilepath);
        this.startSymbol = FileUtils.readStartSymbolFromFile(startSymbolFilepath);
        this.rules = FileUtils.readProductionRulesFromFile(ruleFilepath, terminals, nonTerminals);
        this.table = new SLRParsingTableBuilder(startSymbol, terminals, nonTerminals, rules).build();     // build SLR Parsing Table
//        System.out.println(table);
    }

    public LinkedList<ProductionRule> parse(List<Terminal> tokens) {

        LinkedList<Symbol> symbolStack = new LinkedList<>();
        LinkedList<Integer> stateStack = new LinkedList<>();
        // 初始化stateStack & symbolStack
        stateStack.push(0);
        symbolStack.push(Terminal.END);

        // 初始化tokens，添加END结尾
        tokens = new LinkedList<>(tokens);
        tokens.add(Terminal.END);

        // 初始化输出的产生式列表，空列表
        LinkedList<ProductionRule> output = new LinkedList<>();

        // 初始化当前扫描到的tokens中的current pos为0
        int current = 0;
        while(!stateStack.isEmpty() && current < tokens.size()) {
            Terminal token = tokens.get(current);
            Integer state = stateStack.peek();
            Action action = table.get(state, token);        // 根据栈顶的token和state获取action
            if (action.isShift()) {                         // 移入操作
                symbolStack.push(token);
                stateStack.push(action.getNum());
                current++;
            } else if(action.isReduce()) {                  // 规约操作
                ProductionRule rule = rules.get(action.getNum());
                for (int i = 0; i < rule.getRight().size(); i++) {          // 将规约到的符号和状态移出栈
                    if(!rule.getRight().get(i).equals(Symbol.EMPTY)) {
                        symbolStack.pop();
                        stateStack.pop();
                    }
                }
                symbolStack.push(rule.getLeft());           // 将产生式左边的非终结符移入栈顶，并且移入新的状态
                stateStack.push(table.get(stateStack.peek(), rule.getLeft()));
                output.push(rule);                          // 输出的产生式列表push进该规约操作匹配的产生式
            } else if(action.isAccept()){                   // Accept操作
                ProductionRule rule = rules.get(action.getNum());
                output.push(rule);
                return output;
            }
        }
        throw new SLRParsingException("Not experience Accept Action. ");
    }

    // 生成 right-most derivation 过程
    public String generateDerivationString(List<ProductionRule> rules){
        LinkedList<ProductionRule> ruleStack = new LinkedList<>(rules);
        LinkedList<Symbol> symbolList = new LinkedList<>();
        Symbol startSymbol = Objects.requireNonNull(ruleStack.peek()).getLeft();
        // 将起始符号移入符号列表，并且添加到StringBuilder中
        symbolList.add(startSymbol);
        StringBuilder sb = new StringBuilder();
        sb.append(startSymbol);
        // 如果产生式栈不为空，就一直执行替换操作
        while(!ruleStack.isEmpty()) {
            ProductionRule rule = ruleStack.pop();
            // 从右到左匹配产生式左边的非终结符
            for (int i = symbolList.size() - 1; i >= 0; i--) {
                // 如果匹配到，则移除该产生式左边的非终结符，并移入该产生式右边的符号
                if (symbolList.get(i).equals(rule.getLeft())) {
                    symbolList.remove(i);
                    if (!rule.isDeriveEmpty())
                        symbolList.addAll(i, rule.getRight());
                    break;
                }
            }
            // 输出本次递推的得出的结果
            sb.append("\n => ");
            for (Symbol symbol : symbolList)
                sb.append(symbol).append(" ");
        }
        return sb.toString();
    }


}
