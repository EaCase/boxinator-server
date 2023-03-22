package com.example.boxinator.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateTimeUtils {

    private DateTimeUtils() {
    }


    /**
     * @param str yyyy-MM-dd
     * @return Date
     */
    public static LocalDate fromString(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(str, formatter);
    }

    public static LocalDate now() {
        return LocalDate.now();
    }
}
