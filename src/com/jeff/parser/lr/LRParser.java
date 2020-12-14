package com.jeff.parser.lr;

import com.jeff.FileUtils;
import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

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

        SLRTableParser  tableParser = new SLRTableParser(startSymbol, terminals, nonTerminals, rules);
        this.table = tableParser.parse();
    }


    private NonTerminal readStartSymbolFromFile(String filepath) throws IOException {
        String symbol = FileUtils.readAll(filepath);
        Symbol startSymbol = new NonTerminal(symbol);
        return nonTerminals.get(startSymbol);
    }

    private Map<Terminal, Terminal> readTerminalsFromFile(String filepath) throws IOException {
        String all = FileUtils.readAll(filepath);
        Map<Terminal, Terminal> terminals = Arrays.asList(all.split("\\s+"))
                .stream().map(s -> new Terminal(s)).collect(Collectors.toMap(Function.identity(), Function.identity()));
        Terminal end = new Terminal(Terminal.END.getValue());
        terminals.put(end, end);
        return terminals;
    }

    private Map<NonTerminal, NonTerminal> readNonTerminalsFromFile(String filepath) throws IOException {
        String all = FileUtils.readAll(filepath);
        Map<NonTerminal, NonTerminal> nonTerminals = Arrays.asList(all.split("\\s+"))
                .stream().map(s -> new NonTerminal(s)).collect(Collectors.toMap(Function.identity(), Function.identity()));
        return nonTerminals;
    }

    private List<ProductionRule> readProductionRulesFromFile(String filepath) throws IOException {
        String all = FileUtils.readAll(filepath);
        List<String> productionStrList = Arrays.asList(all.split("\n"));
        List<ProductionRule> rules = new ArrayList<>();
        for(String production : productionStrList) {
            List<String> tokens = Arrays.asList(production.split("\\s+"));
            if(tokens.size() < 2)
                continue;
            NonTerminal left = new NonTerminal(tokens.get(0));
            left = nonTerminals.getOrDefault(left, left);
            List<String> rightStrList = tokens.subList(1, tokens.size());
            List<Symbol> right = new ArrayList<>();
            for(String str : rightStrList) {
                if (str.equals("~"))
                    right.add(Symbol.EMPTY);
                else if (nonTerminals.keySet().contains(new NonTerminal(str))) {
                    NonTerminal nonTerminal = new NonTerminal(str);
                    right.add(nonTerminals.getOrDefault(nonTerminal, nonTerminal));
                } else {
                    Terminal terminal = new Terminal(str);
                    right.add(terminals.getOrDefault(terminal, terminal));
                }
            }
            rules.add(new ProductionRule(left, right));
        }
        return rules;
    }

    public LinkedList<ProductionRule> parse(List<Terminal> tokens) {
        tokens = new LinkedList<>(tokens);
        tokens.add(Terminal.END);

        LinkedList<Symbol> symbolStack = new LinkedList<>();
        LinkedList<Integer> stateStack = new LinkedList<>();
        stateStack.push(0);
        symbolStack.push(Terminal.END);

        LinkedList<ProductionRule> output = new LinkedList<>();

        int current = 0;
        while(!stateStack.isEmpty() && current < tokens.size()) {
            Terminal token = tokens.get(current);
            Integer state = stateStack.peek();
            Action action = table.get(state, token);
            if (action.getType().equals(ActionType.SHIFT)) {
                symbolStack.push(token);
                stateStack.push(action.getNum());
                current++;
            } else if(action.getType().equals(ActionType.REDUCE)){
                ProductionRule rule = rules.get(action.getNum());
                if(!rule.getRight().get(0).equals(Symbol.EMPTY)) {
                    for (int i = 0; i < rule.getRight().size(); i++) {
                        symbolStack.pop();
                        stateStack.pop();
                    }
                }
                state = stateStack.peek();
                symbolStack.push(rule.getLeft());
                stateStack.push(table.get(state, rule.getLeft()));
                output.push(rule);
            } else {
                ProductionRule rule = rules.get(action.getNum());
                output.push(rule);
                return output;
            }
        }
        return null;
    }


    public static void main(String[] args) throws IOException {

        LRParser parser = new LRParser("start-symbol", "terminals", "non-terminals", "rules");
        String context = "{ ID = NUM ; }";
        List<Terminal> tokens = Arrays.asList(context.split("\\s+"))
                .stream().map(s -> new Terminal(s)).collect(Collectors.toList());
        LinkedList<ProductionRule> output = parser.parse(tokens);

        LinkedList<Symbol> symbolList = new LinkedList<>();
        symbolList.add(output.peek().getLeft());


        StringBuffer sb = new StringBuffer(output.peek().getLeft().getValue());

        for(ProductionRule rule : output) {
            for(int i = symbolList.size() - 1; i >= 0; i--) {
                if(symbolList.get(i).equals(rule.getLeft())) {
                    symbolList.remove(i);
                    if(!rule.getRight().get(0).equals(Symbol.EMPTY))
                        symbolList.addAll(i, rule.getRight());
                    break;
                }
            }
            sb.append("\n => ");
            for(Symbol symbol : symbolList)
                sb.append(symbol.getValue()).append(" ");
        }
        System.out.print(sb.toString());

    }

}
