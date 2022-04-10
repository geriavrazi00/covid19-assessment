package com.assessment.covid19.controllers;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.services.CovidDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("covid-data")
@Slf4j
public class CovidDataController {

    @Autowired
    private CovidDataService dataService;

    @GetMapping
    public List<CountryCases> get(@RequestParam Optional<String[]> countries, @RequestParam Optional<String[]> continents) {
        log.info("CovidDataController: Retrieving data");
        return this.dataService.calculate(countries, continents);
    }
}
