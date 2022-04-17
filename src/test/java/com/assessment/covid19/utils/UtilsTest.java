package com.assessment.covid19.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertNull(Utils.decimalPointConverter(null, 2));
    }

    @Test
    void decimalPointConverterWithZeroQuantity() {
        Double number = 0.123456789;
        Double expected = 0.0;
        Double result = Utils.decimalPointConverter(number, 0);
        assertEquals(expected, result);
    }

    @Test
    void decimalPointConverterWithNegativeQuantity() {
        Double number = 0.123456789;
        Double expected = 0.0;
        Double result = Utils.decimalPointConverter(number, -1);
        assertEquals(expected, result);
    }
}