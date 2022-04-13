package com.assessment.covid19.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DataResponse {
    private List<String> selectedCountries;
    private Double correlationCoefficient;
}
