package com.jeff.parser.ll;

import com.jeff.StringUtils;
import com.jeff.parser.NonTerminal;
import com.jeff.parser.ProductionRule;
import com.jeff.parser.Symbol;
import com.jeff.parser.Terminal;

import java.util.*;

public class LLParser {
    // 输出的产生式列表
    private List<ProductionRule> ruleList = new LinkedList<>();

    // 文本匹配
    public boolean parse(String context) {
        // symbol栈
        LinkedList<Symbol> symbolStack = new LinkedList<>();
        // 栈底为END($)
        symbolStack.push(Terminal.END);
        // 初始栈顶为Start Symbol
        symbolStack.push(LLParsingTable.PROGRAM);

        // 文本以空格作为分隔符分为多个token，每个token都是终结符，在列表尾部添加END($)作为结尾
        LinkedList<Symbol> tokenStack = new LinkedList<>();
        for(String token : StringUtils.separateBySpace(context))
            tokenStack.add(new Terminal(token));
        tokenStack.add(Terminal.END);

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
                ProductionRule rule = LLParsingTable.table.getOrDefault(top, new HashMap<>()).get(token);
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
        recursiveOutputParseTree(new LinkedList<>(Collections.singletonList(LLParsingTable.PROGRAM)), new LinkedList<>(ruleList), 0, space);
    }


    /**
     * 递归输出解释树
     * @param symbolList 该层的所有符号的列表
     * @param ruleList 剩余的产生式
     * @param layer 缩进的层数，第一层缩进0层
     * @param space tab的长度
     */
    private void recursiveOutputParseTree(LinkedList<Symbol> symbolList, LinkedList<ProductionRule> ruleList, int layer, int space) {
        // 遍历该层的所有符号
        for (Symbol symbol : symbolList) {
            // 输出缩进和符号
            for(int i = 0; i < layer*space; i++)
                System.out.print(" ");
            System.out.println(symbol.getValue());
            // 如果该符号为非终结符，则用栈顶的产生式匹配该符号，并移除栈顶，递归进入下一层
            if(symbol instanceof NonTerminal) {
                ProductionRule rule = ruleList.pop();
                recursiveOutputParseTree(new LinkedList<>(rule.getRight()), ruleList, layer + 1, space);
            }
        }
    }

}
