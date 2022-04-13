package com.assessment.covid19.services;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.models.DataResponse;
import com.assessment.covid19.repositories.CovidDataRepository;
import com.assessment.covid19.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CovidDataService {

    @Autowired
    private CovidDataRepository covidCasesRepository;

    public DataResponse calculateCorrelationCoefficient(Optional<String[]> countries, Optional<String[]> continents) {
        log.info("CovidDataService: calculateCorrelationCoefficient()");

        Triple<List<String>, List<CountryCases> , List<CountryVaccines>> countryData;

        if (continents.isPresent()) {
            countryData = this.covidCasesRepository.filterCasesByContinents(continents.get());
        } else if (countries.isPresent()) {
            countryData = this.covidCasesRepository.filterCasesByCountries(countries.get());
        } else {
            countryData = this.covidCasesRepository.getAllCases();
        }

        List<Double> xPercentages = new ArrayList<>();
        List<Double> yPercentages = new ArrayList<>();

        countryData.getMiddle().forEach(countryCase -> xPercentages.add(countryCase.calculateDeathsToPopPercentage()));
        countryData.getRight().forEach(countryVaccination -> yPercentages.add(countryVaccination.calculateVaxToPopPercentage()));

        Double correlationCoefficient = this.correlationCoefficient(xPercentages, yPercentages);
        DataResponse response = DataResponse.builder()
                .correlationCoefficient(Utils.decimalPointConverter(correlationCoefficient, 4))
                .selectedCountries(countryData.getLeft())
                .build();

        log.info("CovidDataService: calculateCorrelationCoefficient() finished");

        return response;
    }

    private Double correlationCoefficient(List<Double> x, List<Double> y) {
        int n = x.size();
        Double xSum = 0.0;
        Double ySum = 0.0;
        Double xySum = 0.0;
        Double xSquaredSum = 0.0;
        Double ySquaredSum = 0.0;

        for (int i = 0; i < n; i++) {
            Double elementX = x.get(i);
            Double elementY = y.get(i);

            xSum += elementX; // sum of elements of list x
            ySum += elementY; // sum of elements of list y
            xySum += elementX * elementY; // sum of x[i] * y[i]

            // sum of square of array elements
            xSquaredSum += elementX * elementX;
            ySquaredSum += elementY * elementY;
        }

        // use formula for calculating correlation coefficient
        Double numerator = n * xySum - xSum * ySum;
        Double denominator = (n * xSquaredSum - xSum * xSum) * (n * ySquaredSum - ySum * ySum);
        return denominator != 0 ? numerator / (Math.sqrt(denominator)) : null;
    }
}
