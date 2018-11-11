package com.hpe.tokenizer.utils;

public class Utils {
    public static String skipWhitespaces(String text) {
        return text.replaceAll("^\\s+", "");
    }
}
