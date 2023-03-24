package com.example.boxinator.utils;

import java.util.regex.Pattern;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isEmail(String string) {
        return Pattern.compile("^(.+)@(\\S+)$")
                .matcher(string)
                .matches();
    }
}
