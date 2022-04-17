package com.assessment.covid19.repositories;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import com.assessment.covid19.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
/*
 * The class is built as a repository, since this is the place we are getting the data. It is not a classic repository
 * since there is no connection to the database in this application.
 */
public class CovidDataRepository {

    @Autowired
    private ExternalCommunicationService communicationService;

    /**
     * Getting all data when no filters for countries or continents are provided.
     *
     * @return
     */
    public Triple<List<String>, List<CountryCases>, List<CountryVaccines>> getAllCases() {
        log.info("CovidDataRepository: getAllCases()");
        List<String> countryNames = new ArrayList<>(communicationService.getCountryCasesMap().keySet());
        return this.filterCasesByCountries(countryNames.toArray(new String[0]));
    }

    /**
     * Getting all the data based on the country filters provided by the user. The result of the method will be
     * a triple containing a list of the countries selected, a list of the cases for the selected countries and
     * a list of the vaccines for the selected countries. There were cases when the countries between the countries
     * and vaccines data did not always match. Thus, I have added the control inside the countries loop,
     * the countries should be present in both lists to be able to make accurate calculations.
     *
     * @param countries
     * @return
     */
    public Triple<List<String>, List<CountryCases>, List<CountryVaccines>> filterCasesByCountries(String[] countries) {
        log.info("CovidDataRepository: filterCasesByCountries() for these countries: {}", Arrays.toString(countries));

        List<String> countryNames = new ArrayList<>();
        List<CountryCases> countryCases = new ArrayList<>();
        List<CountryVaccines> countryVaccines = new ArrayList<>();

        for (String country: countries) {
            CountryCases countryCase = communicationService.getCountryCasesMap().get(country.toLowerCase());
            CountryVaccines countryVaccine = communicationService.getCountryVaccinesMap().get(country.toLowerCase());

            if (countryCase != null && countryVaccine != null) {
                countryNames.add(Utils.formatName(country));
                countryCases.add(countryCase);
                countryVaccines.add(countryVaccine);
            }
        }

        Collections.sort(countryNames);

        return Triple.of(countryNames, countryCases, countryVaccines);
    }

    /**
     * Getting all the data based on the continent filters provided by the user
     *
     * @param continents
     * @return
     */
    public Triple<List<String>, List<CountryCases>, List<CountryVaccines>> filterCasesByContinents(String[] continents) {
        log.info("CovidDataRepository: filterCasesByContinents() for these continents: {}", Arrays.toString(continents));

        List<String> countryNames = new ArrayList<>();

        for (String continent: continents) {
            List<CountryCases> countryCases = communicationService.getContinentCasesMap().get(continent.toLowerCase());

            if (countryCases != null && !countryCases.isEmpty()) {
                countryCases.forEach(countryCase -> countryNames.add(countryCase.getCountry().toLowerCase()));
            }
        }

        return this.filterCasesByCountries(countryNames.toArray(new String[0]));
    }
}
