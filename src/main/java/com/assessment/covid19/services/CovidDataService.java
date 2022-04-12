package com.assessment.covid19.services;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.repositories.CovidCasesRepository;
import com.assessment.covid19.repositories.CovidVaccinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CovidDataService {

    @Autowired
    private CovidCasesRepository covidCasesRepository;

    @Autowired
    private CovidVaccinesRepository covidVaccinesRepository;

    public List<CountryCases> calculate(Optional<String[]> countries, Optional<String[]> continents) {
        List<CountryCases> countryCases;
        List<CountryVaccines> countryVaccines;

        if (continents.isPresent()) {
            countryCases = this.covidCasesRepository.filterCasesByContinents(continents.get());
            countryVaccines = this.covidVaccinesRepository.filterVaccinesByContinents(continents.get());
        } else if (countries.isPresent()) {
            countryCases = this.covidCasesRepository.filterCasesByCountries(countries.get());
        } else {
            countryCases = this.covidCasesRepository.getAllCases();
        }

        return countryCases;
    }

}
