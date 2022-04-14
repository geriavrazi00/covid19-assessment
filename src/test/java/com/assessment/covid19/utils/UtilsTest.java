package com.assessment.covid19.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class UtilsTest {

    @Test
    void formatNameWithFullString() {
        String name = "europe";
        String result = Utils.formatName(name);
        Assert.assertEquals("Europe", result);
    }

    @Test
    void formatNameWithNullString() {
        String result = Utils.formatName(null);
        Assert.assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    void formatNameWithEmptyString() {
        String result = Utils.formatName("");
        Assert.assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    void decimalPointConverterWithNumber() {
        Double number = 0.123456789;
        Double expected = 0.12;
        Double result = Utils.decimalPointConverter(number, 2);
        Assert.assertEquals(expected, result);
    }

    @Test
    void decimalPointConverterWithNullNumber() {
        Double result = Utils.decimalPointConverter(null, 2);
        Assert.assertEquals(null, result);
    }

    @Test
    void decimalPointConverterWithZeroQuantity() {
        Double number = 0.123456789;
        Double expected = 0.0;
        Double result = Utils.decimalPointConverter(number, 0);
        Assert.assertEquals(expected, result);
    }

    @Test
    void decimalPointConverterWithNegativeQuantity() {
        Double number = 0.123456789;
        Double expected = 0.0;
        Double result = Utils.decimalPointConverter(number, -1);
        Assert.assertEquals(expected, result);
    }
}