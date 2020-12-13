package com.jeff.parser.ll;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionsRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.util.*;

public class LLParser {
    // parsing table
    private Map<NonTerminal, Map<Terminal, ProductionsRule>> table = new HashMap<>();
    // 输出的产生式列表
    private List<ProductionsRule> ruleList = new LinkedList<>();
    {
        // 初始化parsing table
        table.put(NonTerminal.PROGRAM, new HashMap<>());
        table.put(NonTerminal.STMT, new HashMap<>());
        table.put(NonTerminal.COMPOUND_STMT, new HashMap<>());
        table.put(NonTerminal.STMTS, new HashMap<>());
        table.put(NonTerminal.IF_STMT, new HashMap<>());
        table.put(NonTerminal.WHILE_STMT, new HashMap<>());
        table.put(NonTerminal.ASSG_STMT, new HashMap<>());
        table.put(NonTerminal.BOOL_EXPR, new HashMap<>());
        table.put(NonTerminal.BOOL_OP, new HashMap<>());
        table.put(NonTerminal.ARITH_EXPR, new HashMap<>());
        table.put(NonTerminal.ARITH_EXPR_PRIME, new HashMap<>());
        table.put(NonTerminal.MULT_EXPR, new HashMap<>());
        table.put(NonTerminal.MULT_EXPR_PRIME, new HashMap<>());
        table.put(NonTerminal.SIMPLE_EXPR, new HashMap<>());

        table.get(NonTerminal.PROGRAM).put(Terminal.OPEN_BRACE, ProductionsRule.P1);
        table.get(NonTerminal.STMT).put(Terminal.IF, ProductionsRule.P2);
        table.get(NonTerminal.STMT).put(Terminal.WHILE, ProductionsRule.P3);
        table.get(NonTerminal.STMT).put(Terminal.ID, ProductionsRule.P4);
        table.get(NonTerminal.STMT).put(Terminal.OPEN_BRACE, ProductionsRule.P5);
        table.get(NonTerminal.COMPOUND_STMT).put(Terminal.OPEN_BRACE, ProductionsRule.P6);
        table.get(NonTerminal.STMTS).put(Terminal.OPEN_BRACE, ProductionsRule.P7);
        table.get(NonTerminal.STMTS).put(Terminal.IF, ProductionsRule.P7);
        table.get(NonTerminal.STMTS).put(Terminal.WHILE, ProductionsRule.P7);
        table.get(NonTerminal.STMTS).put(Terminal.ID, ProductionsRule.P7);
        table.get(NonTerminal.STMTS).put(Terminal.CLOSE_BRACE, ProductionsRule.P8);
        table.get(NonTerminal.IF_STMT).put(Terminal.IF, ProductionsRule.P9);
        table.get(NonTerminal.WHILE_STMT).put(Terminal.WHILE, ProductionsRule.P10);
        table.get(NonTerminal.ASSG_STMT).put(Terminal.ID, ProductionsRule.P11);
        table.get(NonTerminal.BOOL_EXPR).put(Terminal.OPEN_PARENTHESES, ProductionsRule.P12);
        table.get(NonTerminal.BOOL_EXPR).put(Terminal.ID, ProductionsRule.P12);
        table.get(NonTerminal.BOOL_EXPR).put(Terminal.NUM, ProductionsRule.P12);
        table.get(NonTerminal.BOOL_OP).put(Terminal.LESS_THAN, ProductionsRule.P13);
        table.get(NonTerminal.BOOL_OP).put(Terminal.GREATER_THAN, ProductionsRule.P14);
        table.get(NonTerminal.BOOL_OP).put(Terminal.LESS_THAN_OR_EQUAL, ProductionsRule.P15);
        table.get(NonTerminal.BOOL_OP).put(Terminal.GREATER_THAN_OR_EQUAL, ProductionsRule.P16);
        table.get(NonTerminal.BOOL_OP).put(Terminal.EQUAL, ProductionsRule.P17);
        table.get(NonTerminal.ARITH_EXPR).put(Terminal.OPEN_PARENTHESES, ProductionsRule.P18);
        table.get(NonTerminal.ARITH_EXPR).put(Terminal.ID, ProductionsRule.P18);
        table.get(NonTerminal.ARITH_EXPR).put(Terminal.NUM, ProductionsRule.P18);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.PLUS, ProductionsRule.P19);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.MINUS, ProductionsRule.P20);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.CLOSE_PARENTHESES, ProductionsRule.P21);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.SEMICOLON, ProductionsRule.P21);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.LESS_THAN, ProductionsRule.P21);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.GREATER_THAN, ProductionsRule.P21);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.LESS_THAN_OR_EQUAL, ProductionsRule.P21);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.GREATER_THAN_OR_EQUAL, ProductionsRule.P21);
        table.get(NonTerminal.ARITH_EXPR_PRIME).put(Terminal.EQUAL, ProductionsRule.P21);
        table.get(NonTerminal.MULT_EXPR).put(Terminal.OPEN_PARENTHESES, ProductionsRule.P22);
        table.get(NonTerminal.MULT_EXPR).put(Terminal.ID, ProductionsRule.P22);
        table.get(NonTerminal.MULT_EXPR).put(Terminal.NUM, ProductionsRule.P22);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.MULTIPLY, ProductionsRule.P23);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.DIVIDE, ProductionsRule.P24);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.CLOSE_PARENTHESES, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.SEMICOLON, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.LESS_THAN, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.GREATER_THAN, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.LESS_THAN_OR_EQUAL, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.GREATER_THAN_OR_EQUAL, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.EQUAL, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.PLUS, ProductionsRule.P25);
        table.get(NonTerminal.MULT_EXPR_PRIME).put(Terminal.MINUS, ProductionsRule.P25);
        table.get(NonTerminal.SIMPLE_EXPR).put(Terminal.ID, ProductionsRule.P26);
        table.get(NonTerminal.SIMPLE_EXPR).put(Terminal.NUM, ProductionsRule.P27);
        table.get(NonTerminal.SIMPLE_EXPR).put(Terminal.OPEN_PARENTHESES, ProductionsRule.P28);
    }

    // 文本匹配
    public boolean match(String context) {
        // symbol栈
        LinkedList<Symbol> symbolStack = new LinkedList<>();
        // 栈底为END($)
        symbolStack.push(Terminal.END);
        // 初始栈顶为Start Symbol
        symbolStack.push(NonTerminal.PROGRAM);

        // 文本以空格作为分隔符分为多个token，每个token都是终结符，在列表尾部添加END($)作为结尾
        LinkedList<Symbol> tokenStack = new LinkedList<>();
        for(String token : context.split(" "))
            tokenStack.add(new Terminal(token));
        tokenStack.add(Symbol.END);

        /*
         * 最终情况1：匹配直到栈空，匹配成功；
         * 最终情况2：匹配完所有token，但栈未空，匹配失败；
         * 最终情况3：栈顶为终结符，栈顶的终结符与token不一致，匹配失败；
         * 最终情况4：栈顶为非终结符，token不是栈顶非终结符的起始终结符，匹配失败；
         */
        // 匹配直到栈为空或者tokenList为空为止
        while(!symbolStack.isEmpty() && !tokenStack.isEmpty()) {
            // 获取栈顶符号
            Symbol top = symbolStack.peek();
            // 获取当前的token
            Symbol token = tokenStack.peek();
            /*
             * 如果栈顶是终结符，则存在三种情况
             * 情况1：top == token 移除栈顶，当前token已被匹配掉；
             * 情况2：top != token, top == epsilon，移除栈顶，当前token尚未被匹配；
             * 情况3：top != token, top != epsilon，匹配失败
             */
            if(top instanceof Terminal) {
                if(token.equals(top)) {
                    symbolStack.pop();
                    tokenStack.pop();
                } else {
                    return false;
                }
            /*
             * 如果栈顶是非终结符，则取出该非中介符以token为起始终结符的产生式
             * 情况1：该产生式不存在，则匹配失败；
             * 情况2：该产生式存在，则移除栈顶，将产生式右侧的符号列表以从右到左的顺序压栈，并将产生式加入产生式列表
             */
            } else if(top instanceof NonTerminal){
                ProductionsRule rule = table.getOrDefault(top, new HashMap<>()).get(token);
                if(rule == null)
                    return false;
                symbolStack.pop();
                List<Symbol> symbolList = rule.getRight();
                for (int i = symbolList.size() - 1; i >= 0; i--)
                    symbolStack.push(symbolList.get(i));
                ruleList.add(rule);
            } else {
                if(Symbol.EMPTY.equals(top)){
                    symbolStack.pop();
                } else if(Symbol.END.equals(top)) {
                    symbolStack.pop();
                    tokenStack.pop();
                }
            }
        }
        return true;
    }

    /**
     * 输出解释树
     * @param space tab长度
     */
    public void outputParseTree(int space){
        recursiveOutputParseTree(new LinkedList<>(Collections.singletonList(NonTerminal.PROGRAM)), new LinkedList<>(ruleList), 0, space);
    }


    /**
     * 递归输出解释树
     * @param symbolList 该层的所有符号的列表
     * @param ruleList 剩余的产生式
     * @param layer 缩进的层数，第一层缩进0层
     * @param space tab的长度
     */
    private void recursiveOutputParseTree(LinkedList<Symbol> symbolList, LinkedList<ProductionsRule> ruleList, int layer, int space) {
        // 遍历该层的所有符号
        for (Symbol symbol : symbolList) {
            // 输出缩进和符号
            for(int i = 0; i < layer*space; i++)
                System.out.print(" ");
            System.out.println(symbol.getValue());
            // 如果该符号为非终结符，则用栈顶的产生式匹配该符号，并移除栈顶，递归进入下一层
            if(symbol instanceof NonTerminal) {
                ProductionsRule rule = ruleList.pop();
                recursiveOutputParseTree(new LinkedList<>(rule.getRight()), ruleList, layer + 1, space);
            }
        }
    }

}
