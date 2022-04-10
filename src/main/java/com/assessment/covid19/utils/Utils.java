package com.assessment.covid19.utils;

import org.apache.commons.lang3.StringUtils;

public class Utils {

    private Utils() {}

    public static String formatName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        return StringUtils.EMPTY;
    }
}
