package com.assessment.covid19.repositories;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CovidDataRepository {

    @Autowired
    private ExternalCommunicationService communicationService;

    public List<CountryCases> filterDataByCountries(Optional<String[]> countries) {
        List<CountryCases> countryCases = new ArrayList<>();

        if (countries.isPresent()) {
            for (String country: countries.get()) {
                if (communicationService.getCountryCasesMap().containsKey(country)) {
                    countryCases.add(communicationService.getCountryCasesMap().get(country));
                }
            }
        } else {
            countryCases = new ArrayList<>(communicationService.getCountryCasesMap().values());
        }

        return countryCases;
    }
}
