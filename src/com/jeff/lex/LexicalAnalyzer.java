package com.jeff.lex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private static final Pattern KEYWORD = Pattern.compile("^((int)|(real)|(if)|(then)|(else)|(while))$");
    private static final Pattern DELIMITER = Pattern.compile("^([(){};,])$");
    private static final Pattern NUMBER = Pattern.compile("^((\\d+\\.\\d+([Ee]([+\\-])?\\d+)?)|(\\d+[Ee]([+\\-])?\\d+)|(\\d+))$");
    private static final Pattern OPERATOR = Pattern.compile("^((==)|(<=)|(>=)|(!=)|[+\\-/*=<>])$");
    private static final Pattern IDENTIFIER = Pattern.compile("^([a-zA-Z][a-zA-Z0-9]*)$");

    /**
     * 根据属性值判断token类型
     * @param value 属性值
     * @return token类型字符串
     */
    public static String getTokenType(String value){
        Matcher keywordMatcher = KEYWORD.matcher(value);
        Matcher delimiterMatcher = DELIMITER.matcher(value);
        Matcher numberMatcher = NUMBER.matcher(value);
        Matcher operatorMatcher = OPERATOR.matcher(value);
        Matcher identifierMatcher = IDENTIFIER.matcher(value);
        if(delimiterMatcher.find())
            return "delimiters";
        else if(keywordMatcher.find())
            return "keywords";
        else if(numberMatcher.find())
            return "numbers";
        else if(operatorMatcher.find())
            return "operators";
        else if(identifierMatcher.find())
            return "identifiers";
        return "errors";
    }

}
