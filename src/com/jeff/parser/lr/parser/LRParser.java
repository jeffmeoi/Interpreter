package com.jeff.parser.lr.parser;

import com.jeff.parser.*;
import com.jeff.parser.lr.table.Action;
import com.jeff.parser.lr.table.SLRParsingTable;
import com.jeff.parser.lr.table.SLRTableMaker;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LRParser {

    private final Symbol startSymbol = LRConstant.startSymbol;
    private final Map<Terminal, Terminal> terminals = LRConstant.terminals;
    private final Map<NonTerminal, NonTerminal> nonTerminals = LRConstant.nonTerminals;
    private final List<ProductionRule> rules = LRConstant.rules;
    private final SLRParsingTable table;

    public LRParser() throws IOException {
        SLRTableMaker tableParser = new SLRTableMaker(startSymbol, terminals, nonTerminals, rules);
        this.table = tableParser.make();
//        System.out.println(table);
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
                    if(!rule.getRight().get(i).equals(Symbol.EMPTY)) {
                        symbolStack.pop();
                        stateStack.pop();
                    }
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

        LRParser parser = new LRParser();
        String context = "{ ID = NUM ; }"; 
        List<Terminal> tokens = Arrays.asList(context.split("\\s+")).stream().map(s -> new Terminal(s)).collect(Collectors.toList());
        LinkedList<ProductionRule> ruleStack = parser.parse(tokens);
        System.out.println(parser.generateDerivationString(ruleStack));
    }

}
