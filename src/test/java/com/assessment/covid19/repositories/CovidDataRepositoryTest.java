package com.assessment.covid19.repositories;

import com.assessment.covid19.MockGenerator;
import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import com.assessment.covid19.utils.Utils;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class CovidDataRepositoryTest {
    @Spy
    @InjectMocks
    private CovidDataRepository dataRepository;

    @Mock
    private ExternalCommunicationService communicationService;

    private void initMocks(Optional<String[]> countries, boolean empty) {
        this.initCountryCases(countries, empty);
        this.initCountryVaccines(countries, empty);
    }

    private void initCountryCases(Optional<String[]> countries, boolean empty) {
        Mockito.when(communicationService.getCountryCasesMap()).thenReturn(empty ? new HashMap<>() : MockGenerator.buildMapOfCountryCases(countries));
    }

    private void initCountryVaccines(Optional<String[]> countries, boolean empty) {
        Mockito.when(communicationService.getCountryVaccinesMap()).thenReturn(empty ? new HashMap<>() : MockGenerator.buildMapOfCountryVaccines(countries));
    }

    @Test
    public void filterByCountries() {
        String[] countries = MockGenerator.NORTH_AMERICAN_COUNTRIES;
        initMocks(Optional.of(countries), false);
        String[] countriesToFilter = new String[] {
            Utils.formatName(MockGenerator.NORTH_AMERICAN_COUNTRIES[0]),
            Utils.formatName(MockGenerator.NORTH_AMERICAN_COUNTRIES[1])
        };
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByCountries(countriesToFilter);

        Assert.assertTrue(result.getLeft().size() == countriesToFilter.length
            && Arrays.stream(countriesToFilter).allMatch(s -> result.getLeft().contains(s))
            && result.getMiddle().size() == countriesToFilter.length && result.getRight().size() == countriesToFilter.length
        );
    }

    @Test
    public void filterByCountriesWithoutCountries() {
        initMocks(Optional.empty(), true);
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByCountries(new String[]{});
        Assert.assertTrue(result.getLeft().isEmpty() && result.getMiddle().isEmpty() && result.getRight().isEmpty());
    }

    @Test
    public void filterByCountriesWithCasesOnly() {
        String[] countries = MockGenerator.NORTH_AMERICAN_COUNTRIES;
        initCountryCases(Optional.of(countries), false);
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByCountries(countries);
        Assert.assertTrue(result.getLeft().isEmpty() && result.getMiddle().isEmpty() && result.getRight().isEmpty());
    }

    @Test
    public void filterByCountriesWithVaccinesOnly() {
        String[] countries = MockGenerator.NORTH_AMERICAN_COUNTRIES;
        initCountryVaccines(Optional.of(countries), false);
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByCountries(countries);
        Assert.assertTrue(result.getLeft().isEmpty() && result.getMiddle().isEmpty() && result.getRight().isEmpty());
    }

    @Test
    public void filterByCountriesWithEachCaseDifferentCountries() {
        String[] caseCountries = MockGenerator.NORTH_AMERICAN_COUNTRIES;
        List<String> vaccineCountriesList = new ArrayList<>();
        Collections.addAll(vaccineCountriesList, MockGenerator.NORTH_AMERICAN_COUNTRIES);
        Collections.addAll(vaccineCountriesList, MockGenerator.EUROPEAN_COUNTRIES);
        String[] vaccineCountries = new String[vaccineCountriesList.size()];
        vaccineCountriesList.toArray(vaccineCountries);

        initCountryCases(Optional.of(caseCountries), false);
        initCountryVaccines(Optional.of(vaccineCountries), false);
        String[] countriesToFilter = new String[] {
                Utils.formatName(MockGenerator.NORTH_AMERICAN_COUNTRIES[0]),
                Utils.formatName(MockGenerator.EUROPEAN_COUNTRIES[1])
        };
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByCountries(countriesToFilter);

        Assert.assertTrue(result.getLeft().size() == 1
                && result.getLeft().contains(Utils.formatName(MockGenerator.NORTH_AMERICAN_COUNTRIES[0]))
                && result.getMiddle().size() == 1 && result.getRight().size() == 1
        );
    }

    @Test
    public void getAllCases() {
        String[] countries = MockGenerator.ASIAN_COUNTRIES;
        initMocks(Optional.of(countries), false);
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.getAllCases();

        Assert.assertTrue(result.getLeft().size() == countries.length
                && Arrays.stream(countries).allMatch(s -> result.getLeft().contains(s))
                && result.getMiddle().size() == countries.length && result.getRight().size() == countries.length
        );
    }

    @Test
    public void filterCasesByContinents() {
        Map<String, List<CountryCases>> continentCases = MockGenerator.buildMapOfContinentCases();
        Mockito.when(communicationService.getContinentCasesMap()).thenReturn(continentCases);
        initMocks(Optional.of(MockGenerator.EUROPEAN_COUNTRIES), false);

        String[] continents = new String[] { MockGenerator.CONTINENTS[0] };
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByContinents(continents);
        Assert.assertTrue(result.getLeft().size() == MockGenerator.EUROPEAN_COUNTRIES.length
                && result.getMiddle().size() == MockGenerator.EUROPEAN_COUNTRIES.length
                && result.getRight().size() == MockGenerator.EUROPEAN_COUNTRIES.length);
    }

    @Test
    public void filterCasesByContinentsWithoutContinents() {
        Mockito.when(communicationService.getContinentCasesMap()).thenReturn(new HashMap<>());
        initMocks(Optional.empty(), true);

        String[] continents = MockGenerator.CONTINENTS;
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByContinents(continents);
        Assert.assertTrue(result.getLeft().isEmpty() && result.getMiddle().isEmpty() && result.getRight().isEmpty());
    }

    @Test
    public void filterCasesByContinentsWithEmptyCountryCases() {
        String[] continents = new String[] { MockGenerator.CONTINENTS[0] };
        Map<String, List<CountryCases>> continentCases = MockGenerator.buildMapOfContinentCases();
        continentCases.get(continents[0].toLowerCase()).clear();
        Mockito.when(communicationService.getContinentCasesMap()).thenReturn(continentCases);
        initMocks(Optional.of(MockGenerator.EUROPEAN_COUNTRIES), false);

        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> result = this.dataRepository.filterCasesByContinents(continents);
        Assert.assertTrue(result.getLeft().isEmpty() && result.getMiddle().isEmpty() && result.getRight().isEmpty());
    }
}