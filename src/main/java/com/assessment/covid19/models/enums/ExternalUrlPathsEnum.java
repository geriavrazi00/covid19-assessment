package com.assessment.covid19.models.enums;

import lombok.Getter;

@Getter
public enum ExternalUrlPathsEnum {
    CASES_URL("/cases"),
    VACCINES_URL("/vaccines");

    private final String path;

    ExternalUrlPathsEnum(String path) {
        this.path = path;
    }
}
