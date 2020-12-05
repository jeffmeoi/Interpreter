package com.jeff.lex;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer implements Iterator<Token> {
    // 回车的正则表达式字符串
    private static final Pattern ENTER_REGEX = Pattern.compile("\\n");
    // token的正则表达式字符串
    private static final Pattern TOKEN_REGEX = Pattern.compile("([(){};,])" +                             // 分隔符
            "|(((\\d+\\.\\d+([Ee](\\+|-)?\\d+)?)|(\\d+[Ee](\\+|-)?\\d+)|(\\d+))[a-zA-Z0-9]*)" +     // 可能带字母后缀的数字
            "|([a-zA-Z][a-zA-Z0-9]*)" +                                                             // 关键字或标识符
            "|((==)|(<=)|(>=)|(!=)|[+\\-/*=<>])");                                                  // 操作符

    private Matcher enterMatcher;
    private Matcher tokenMatcher;

    private int cursorPos;     // 光标当前在整个字符串中的位置，默认为0
    private int lineNumber;     // 光标当前所在的行号，默认为1，预设第一行第一个位置为(1, 1)
    private int enterPos;       // 光标的上一行结尾的位置，默认为0

    public Tokenizer(String context) {
        context = preProcess(context);
        cursorPos = 0;
        lineNumber = 1;
        enterPos = 0;
        enterMatcher = ENTER_REGEX.matcher(context);
        tokenMatcher = TOKEN_REGEX.matcher(context);
    }


    /**
     * 字符串预处理，删除注释，即删除从//到回车前一个字符
     * @param context 含注释的字符串
     * @return  不含注释的字符串
     */
    private String preProcess(String context) {
        return context.replaceAll("\\/\\/[^\\n]*", "");     // remove comments
    }

    /**
     * 是否还有下一个token
     * @return true为存在，false为不存在
     */
    @Override
    public boolean hasNext() {
        return tokenMatcher.find(cursorPos);
    }

    /**
     * 返回下一个token
     * @return token，不存在返回null
     */
    public Token next(){
        if(tokenMatcher.find(cursorPos)) {
            String value = tokenMatcher.group();
            int tokenEndPos = tokenMatcher.end();
            int tokenStartPos = tokenMatcher.start();
            if(enterMatcher.find(cursorPos) && enterMatcher.end() < tokenEndPos) {
                lineNumber++;
                enterPos = enterMatcher.end();
            }
            cursorPos = tokenEndPos;
            Token token = new Token(null, value, lineNumber, tokenStartPos - enterPos + 1);
            return token;
        }
        return null;
    }

}
