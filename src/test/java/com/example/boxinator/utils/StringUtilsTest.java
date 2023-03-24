package com.example.boxinator.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void validEmail() {
        assertTrue(StringUtils.isEmail("email@example.com"));
    }

    @Test
    void validEmail2() {
        assertTrue(StringUtils.isEmail("a@a.com"));
    }

    @Test
    void invalidEmail() {
        assertFalse(StringUtils.isEmail("example.com"));
    }

    @Test
    void invalidEmail2() {
        assertFalse(StringUtils.isEmail("admin"));
    }
}