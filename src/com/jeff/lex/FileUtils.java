package com.jeff.lex;

import java.io.*;
import java.util.List;

public class FileUtils {

    /**
     * 文件读入
     * @param filepath 需要读入的文件路径
     * @return 文件的所有内容
     * @throws IOException 文件io异常
     */
    public static String readAll(String filepath) throws IOException {
        InputStream is = new FileInputStream(filepath);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        is.close();
        return new String(bytes);
    }

    /**
     * 写回文件
     * @param filepath 需要写回的文件路径
     * @param tokenList 需要写回的token列表
     * @throws IOException 文件io异常
     */
    public static void writeAll(String filepath, List<Token> tokenList) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
        for(Token token : tokenList) {
            bufferedWriter.write(token.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

}
