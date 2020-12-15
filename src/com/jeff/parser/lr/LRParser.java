package com.jeff.parser.lr;

import com.jeff.FileUtils;
import com.jeff.StringUtils;
import com.jeff.parser.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LRParser {

    private final Symbol startSymbol;
    private final Map<Terminal, Terminal> terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals;
    private final List<ProductionRule> rules;
    private final SLRParsingTable table;

    public LRParser(String startSymbolFilepath, String terminalFilepath, String nonTerminalFilepath, String ruleFilepath) throws IOException {

        this.terminals = readTerminalsFromFile(terminalFilepath);
        this.nonTerminals = readNonTerminalsFromFile(nonTerminalFilepath);
        this.startSymbol = readStartSymbolFromFile(startSymbolFilepath);
        this.rules = readProductionRulesFromFile(ruleFilepath);

        Processor processor = new Processor(startSymbol, terminals, nonTerminals, rules);
        processor.computeAllFirstSet();
        processor.computeAllFollowSet();

        SLRTableMaker tableParser = new SLRTableMaker(startSymbol, terminals, nonTerminals, rules);
        this.table = tableParser.make();
    }


    private NonTerminal readStartSymbolFromFile(String filepath) throws IOException {
        String symbol = FileUtils.read(filepath);
        Symbol startSymbol = new NonTerminal(symbol);
        return nonTerminals.get(startSymbol);
    }

    private Map<Terminal, Terminal> readTerminalsFromFile(String filepath) throws IOException {
        Map<Terminal, Terminal> terminals = StringUtils.separateBySpace(FileUtils.read(filepath))
                .stream().map(s -> new Terminal(s))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        Terminal end = new Terminal(Terminal.END);
        terminals.put(end, end);
        return terminals;
    }

    private Map<NonTerminal, NonTerminal> readNonTerminalsFromFile(String filepath) throws IOException {
        Map<NonTerminal, NonTerminal> nonTerminals = StringUtils.separateBySpace(FileUtils.read(filepath))
                .stream().map(s -> new NonTerminal(s))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        return nonTerminals;
    }

    private List<ProductionRule> readProductionRulesFromFile(String filepath) throws IOException {
        return ProductionRule.getProductionRulesByStringList(StringUtils.separateByLine(FileUtils.read(filepath)), terminals, nonTerminals);
    }

    public LinkedList<ProductionRule> parse(List<Terminal> tokens) {

        LinkedList<Symbol> symbolStack = new LinkedList<>();
        LinkedList<Integer> stateStack = new LinkedList<>();
        stateStack.push(0);
        symbolStack.push(Terminal.END);

        tokens = new LinkedList<>(tokens);
        tokens.add(Terminal.END);

        LinkedList<ProductionRule> output = new LinkedList<>();

        int current = 0;
        while(!stateStack.isEmpty() && current < tokens.size()) {
            Terminal token = tokens.get(current);
            Integer state = stateStack.peek();
            Action action = table.get(state, token);
            if (action.isShift()) {
                symbolStack.push(token);
                stateStack.push(action.getNum());
                current++;
            } else if(action.isReduce()) {
                ProductionRule rule = rules.get(action.getNum());
                for (int i = 0; i < rule.getRight().size(); i++) {
                    symbolStack.pop();
                    stateStack.pop();
                }
                symbolStack.push(rule.getLeft());
                stateStack.push(table.get(stateStack.peek(), rule.getLeft()));
                output.push(rule);
            } else if(action.isAccept()){
                ProductionRule rule = rules.get(action.getNum());
                output.push(rule);
                return output;
            }
        }
        throw new SLRParsingException();
    }

    public String generateDerivationString(List<ProductionRule> rules){
        LinkedList<ProductionRule> ruleStack = new LinkedList<>(rules);
        LinkedList<Symbol> symbolList = new LinkedList<>();
        Symbol startSymbol = ruleStack.peek().getLeft();
        symbolList.add(startSymbol);
        StringBuffer sb = new StringBuffer(startSymbol.getValue());
        while(!ruleStack.isEmpty()) {
            ProductionRule rule = ruleStack.pop();
            for (int i = symbolList.size() - 1; i >= 0; i--) {
                if (symbolList.get(i).equals(rule.getLeft())) {
                    symbolList.remove(i);
                    if (!rule.isDeriveEmpty())
                        symbolList.addAll(i, rule.getRight());
                    break;
                }
            }
            sb.append("\n => ");
            for (Symbol symbol : symbolList)
                sb.append(symbol.getValue()).append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {

        LRParser parser = new LRParser("start-symbol", "terminals", "non-terminals", "rules");
        String context = "{ ID = NUM ; }";
        List<Terminal> tokens = Arrays.asList(context.split("\\s+")).stream().map(s -> new Terminal(s)).collect(Collectors.toList());
        LinkedList<ProductionRule> ruleStack = parser.parse(tokens);
        System.out.println(parser.generateDerivationString(ruleStack));
    }

}
