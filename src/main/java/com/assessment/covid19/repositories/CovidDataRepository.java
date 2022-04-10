package com.assessment.covid19.repositories;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import com.assessment.covid19.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CovidDataRepository {

    @Autowired
    private ExternalCommunicationService communicationService;

    public List<CountryCases> filterDataByCountries(Optional<String[]> countries) {
        List<CountryCases> countryCases = new ArrayList<>();

        if (countries.isPresent()) {
            for (String country: countries.get()) {
                String formattedCountryName = Utils.formatName(country);

                if (communicationService.getCountryCasesMap().containsKey(formattedCountryName)) {
                    countryCases.add(communicationService.getCountryCasesMap().get(formattedCountryName));
                }
            }
        } else {
            countryCases = new ArrayList<>(communicationService.getCountryCasesMap().values());
        }

        return countryCases;
    }
}
