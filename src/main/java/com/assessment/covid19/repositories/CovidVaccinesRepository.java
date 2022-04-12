package com.assessment.covid19.repositories;

import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import com.assessment.covid19.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CovidVaccinesRepository {

    @Autowired
    private ExternalCommunicationService communicationService;

    public List<CountryVaccines> getAllVaccines() {
        return new ArrayList<>(communicationService.getCountryVaccinesMap().values());
    }

    public List<CountryVaccines> filterVaccinesByCountries(String[] countries) {
        List<CountryVaccines> countryVaccines = new ArrayList<>();

        for (String country: countries) {
            String formattedCountryName = Utils.formatName(country);

            if (communicationService.getCountryVaccinesMap().containsKey(formattedCountryName)) {
                countryVaccines.add(communicationService.getCountryVaccinesMap().get(formattedCountryName));
            }
        }

        return countryVaccines;
    }

    public List<CountryVaccines> filterVaccinesByContinents(String[] continents) {
        List<CountryVaccines> countryVaccines = new ArrayList<>();

        for (String continent: continents) {
            String formattedContinentName = Utils.formatName(continent);

            if (communicationService.getContinentVaccinesMap().containsKey(formattedContinentName)) {
                countryVaccines.addAll(communicationService.getContinentVaccinesMap().get(formattedContinentName));
            }
        }

        return countryVaccines;
    }
}
