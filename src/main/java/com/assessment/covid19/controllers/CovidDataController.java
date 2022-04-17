package com.assessment.covid19.controllers;

import com.assessment.covid19.services.CovidDataService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@OpenAPIDefinition(info = @Info(title = "Covid-19 Data", description = "Retrieve the correlation coefficient for the selected countries/continents"))
public class CovidDataController {

    @Autowired
    private CovidDataService dataService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Welcoming message", useReturnTypeSchema = true)
    })
    public String welcome() {
        log.info("CovidDataController: Welcome!");
        return "Hi :)";
    }

    @GetMapping("/correlation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response with the selected countries and the correlation coefficient"),
            @ApiResponse(responseCode = "500", description = "Server error"),
    })
    public ResponseEntity<Object> calculateCorrelationCoefficient(@RequestParam Optional<String[]> countries, @RequestParam Optional<String[]> continents) {
        log.info("CovidDataController: calculateCorrelationCoefficient()");
        return this.dataService.calculateCorrelationCoefficient(countries, continents);
    }
}
