package com.assessment.covid19.controllers;

import com.assessment.covid19.services.CovidDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("covid-data")
@Slf4j
public class CovidDataController {

    @Autowired
    private CovidDataService dataService;

    @GetMapping
    public String welcome() {
        log.info("CovidDataController: Welcome!");
        return "Hi :)";
    }

    @GetMapping("/correlation")
    public ResponseEntity<Object> calculateCorrelationCoefficient(@RequestParam Optional<String[]> countries, @RequestParam Optional<String[]> continents) {
        log.info("CovidDataController: calculateCorrelationCoefficient()");
        return this.dataService.calculateCorrelationCoefficient(countries, continents);
    }
}
