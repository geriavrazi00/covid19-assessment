package com.assessment.covid19.utils;

import jdk.jshell.execution.Util;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

class UtilsTest {

    @Test
    void formatNameWithFullString() {
        String name = "europe";
        String result = Utils.formatName(name);
        assertEquals("Europe", result);
    }

    @Test
    void formatNameWithNullString() {
        String result = Utils.formatName(null);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    void formatNameWithEmptyString() {
        String result = Utils.formatName("");
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    void decimalPointConverterWithNumber() {
        Double number = 0.123456789;
        Double expected = 0.12;
        Double result = Utils.decimalPointConverter(number, 2);
        assertEquals(expected, result);
    }

    @Test
    void decimalPointConverterWithNullNumber() {
        Double result = Utils.decimalPointConverter(null, 2);
        assertEquals(null, result);
    }
}