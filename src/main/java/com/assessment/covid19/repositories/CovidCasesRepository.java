package com.assessment.covid19.repositories;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import com.assessment.covid19.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CovidCasesRepository {

    @Autowired
    private ExternalCommunicationService communicationService;

    public List<CountryCases> getAllCases() {
        return new ArrayList<>(communicationService.getCountryCasesMap().values());
    }

    public List<CountryCases> filterCasesByCountries(String[] countries) {
        List<CountryCases> countryCases = new ArrayList<>();

        for (String country: countries) {
            String formattedCountryName = Utils.formatName(country);

            if (communicationService.getCountryCasesMap().containsKey(formattedCountryName)) {
                countryCases.add(communicationService.getCountryCasesMap().get(formattedCountryName));
            }
        }

        return countryCases;
    }

    public List<CountryCases> filterCasesByContinents(String[] continents) {
        List<CountryCases> countryCases = new ArrayList<>();

        for (String continent: continents) {
            String formattedContinentName = Utils.formatName(continent);

            if (communicationService.getContinentCasesMap().containsKey(formattedContinentName)) {
                countryCases.addAll(communicationService.getContinentCasesMap().get(formattedContinentName));
            }
        }

        return countryCases;
    }
}
