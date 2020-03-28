package com.example.lib_commons.util;

public class StringUtility {

    public static boolean isNullOrEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotNullOrEmpty(CharSequence str) {
        return !isNullOrEmpty(str);
    }
}
