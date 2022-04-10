package com.assessment.covid19.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CountryCases {
    private Long confirmed;
    private Long recovered;
    private Long deaths;
    private String country;
    private Long population;

    @JsonAlias("sq_km_area")
    private Long sqKmArea;

    @JsonAlias("life_expectancy")
    private String lifeExpectancy;

    @JsonAlias("elevation_in_meters")
    private String elevationInMeters;

    private String continent;
    private String abbreviation;
    private String location;
    private Long iso;

    @JsonAlias("capital_city")
    private String capitalCity;

    @JsonAlias("lat")
    private String latitude;

    @JsonAlias("long")
    private String longitude;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updated;
    private List<CountryDivision> divisions;
}
