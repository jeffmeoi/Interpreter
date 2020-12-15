package com.jeff;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static List<String> separateByLine(String context) {
        return Arrays.asList(context.split("\n"));
    }

    public static List<String> separateBySpace(String context) {
        return Arrays.asList(context.split("\\s+"));
    }

}
