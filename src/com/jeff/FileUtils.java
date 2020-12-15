package com.jeff;

import java.io.*;
import java.util.List;

public class FileUtils {

    /**
     * 文件读入
     * @param filepath 需要读入的文件路径
     * @return 文件的所有内容
     * @throws IOException 文件io异常
     */
    public static String read(String filepath) throws IOException {
        InputStream is = new FileInputStream(filepath);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        is.close();
        return new String(bytes);
    }


    /**
     * 写回文件
     * @param filepath 需要写回的文件路径
     * @param string 待输出的字符串
     * @throws IOException 文件io异常
     */
    public static void write(String filepath, String string) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
        bufferedWriter.write(string);
        bufferedWriter.close();
    }

}
