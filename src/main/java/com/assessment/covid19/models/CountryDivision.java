package com.assessment.covid19.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.Date;

@Data
public class CountryDivision {
    private String name;

    @JsonAlias("lat")
    private String latitude;

    @JsonAlias("long")
    private String longitude;

    private Long confirmed;
    private Long recovered;
    private Long deaths;
    private Date updated;
}
