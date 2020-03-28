package com.example.lib_commons.util;

public class StringUtility {

    public static boolean isNullOrEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotNullOrEmpty(CharSequence str) {
        return !isNullOrEmpty(str);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }

        if (str1 == null) {
            return false;
        }

        return str1.equalsIgnoreCase(str2);
    }
}
