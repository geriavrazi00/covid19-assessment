package com.assessment.covid19.services;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.repositories.CovidDataRepository;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CovidDataService {

    @Autowired
    private CovidDataRepository dataRepository;

    public List<CountryCases> calculate(Optional<String[]> countries, Optional<String[]> continents) {
        List<CountryCases> countryCases;

        if (continents.isPresent()) {
            countryCases = this.dataRepository.filterDataByContinents(continents.get());
        } else if (countries.isPresent()) {
            countryCases = this.dataRepository.filterDataByCountries(countries.get());
        } else {
            countryCases = this.dataRepository.getAll();
        }

        return countryCases;
    }

}
