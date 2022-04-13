package com.assessment.covid19.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

public class Utils {

    private Utils() {}

    public static String formatName(String name) {
        StringBuilder formattedName = new StringBuilder();

        if (name != null && !name.trim().isEmpty()) {
            String[] splitName = name.split(" ");

            for (String splitString: splitName) {
                formattedName.append(splitString.substring(0, 1).toUpperCase())
                    .append(splitString.substring(1).toLowerCase())
                    .append(" ");
            }

            return formattedName.toString().trim();
        }

        return StringUtils.EMPTY;
    }

    public static Double decimalPointConverter(Double number, int decimalNumberQuantity) {
        if (number == null) return null;
        DecimalFormat dfSharp = new DecimalFormat("#." + "#".repeat(Math.max(0, decimalNumberQuantity)));
        return Double.valueOf(dfSharp.format(number));
    }
}
