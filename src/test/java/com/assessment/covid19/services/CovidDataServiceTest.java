package com.assessment.covid19.services;

import com.assessment.covid19.MockGenerator;
import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.models.DataResponse;
import com.assessment.covid19.repositories.CovidDataRepository;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CovidDataServiceTest {
    @Spy
    @InjectMocks
    private CovidDataService dataService;

    @Mock
    private CovidDataRepository covidCasesRepository;

    @Test
    public void calculateWithoutInput() {
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> mockedMap = MockGenerator.buildTriple(Optional.empty(), Optional.empty());
        Mockito.when(this.covidCasesRepository.getAllCases()).thenReturn(mockedMap);
        ResponseEntity<Object> response = this.dataService.calculateCorrelationCoefficient(Optional.empty(), Optional.empty());
        DataResponse dataResponse = (DataResponse) response.getBody();
        Double correlation = dataResponse.getCorrelationCoefficient();

        Assert.assertTrue(dataResponse.getSelectedCountries().equals(mockedMap.getLeft()) && correlation >= -1 && correlation <= 1);
    }

    @Test
    public void calculateWithOneCountryOnly() {
        String[] country = new String[] {MockGenerator.EUROPEAN_COUNTRIES[1]};
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> mockedMap = MockGenerator.buildTriple(Optional.of(country), Optional.empty());

        Mockito.when(this.covidCasesRepository.filterCasesByCountries(Mockito.any())).thenReturn(mockedMap);
        ResponseEntity<Object> response = this.dataService.calculateCorrelationCoefficient(Optional.of(country), Optional.empty());
        DataResponse dataResponse = (DataResponse) response.getBody();
        Double correlation = dataResponse.getCorrelationCoefficient();

        Assert.assertTrue(dataResponse.getSelectedCountries().equals(mockedMap.getLeft()) && correlation == null
                && dataResponse.getSelectedCountries().contains(country[0]));
    }

    @Test
    public void calculateWithCountriesOnly() {
        String[] country = MockGenerator.EUROPEAN_COUNTRIES;
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> mockedMap = MockGenerator.buildTriple(Optional.of(country), Optional.empty());

        Mockito.when(this.covidCasesRepository.filterCasesByCountries(Mockito.any())).thenReturn(mockedMap);
        ResponseEntity<Object> response = this.dataService.calculateCorrelationCoefficient(Optional.of(country), Optional.empty());
        DataResponse dataResponse = (DataResponse) response.getBody();
        Double correlation = dataResponse.getCorrelationCoefficient();

        Assert.assertTrue(dataResponse.getSelectedCountries().equals(mockedMap.getLeft()) && correlation >= -1
                && correlation <= 1 && dataResponse.getSelectedCountries().contains(country[0]));
    }

    @Test
    public void calculateWithContinentsOnly() {
        String continent = "Africa";
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> mockedMap = MockGenerator.buildTriple(Optional.empty(), Optional.of(continent));

        Mockito.when(this.covidCasesRepository.filterCasesByContinents(Mockito.any())).thenReturn(mockedMap);
        ResponseEntity<Object> response = this.dataService.calculateCorrelationCoefficient(Optional.empty(), Optional.of(new String[] {continent}));
        DataResponse dataResponse = (DataResponse) response.getBody();
        Double correlation = dataResponse.getCorrelationCoefficient();

        Assert.assertTrue(dataResponse.getSelectedCountries().equals(mockedMap.getLeft()) && correlation >= -1
                && correlation <= 1);
    }

    @Test
    public void calculateWithContinentsAndCountries() {
        String continent = "Asia";
        String[] country = MockGenerator.ASIAN_COUNTRIES;
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> mockedMap = MockGenerator.buildTriple(Optional.of(country), Optional.of(continent));

        Mockito.when(this.covidCasesRepository.filterCasesByContinents(Mockito.any())).thenReturn(mockedMap);
        ResponseEntity<Object> response = this.dataService.calculateCorrelationCoefficient(Optional.of(country), Optional.of(new String[] {continent}));
        DataResponse dataResponse = (DataResponse) response.getBody();
        Double correlation = dataResponse.getCorrelationCoefficient();

        Assert.assertTrue(dataResponse.getSelectedCountries().equals(mockedMap.getLeft()) && correlation >= -1
                && correlation <= 1 && Arrays.stream(country).allMatch(s -> dataResponse.getSelectedCountries().contains(s)));
    }

    @Test
    public void calculateWithoutInputAndEmptyData() {
        Triple<List<String>, List<CountryCases>, List<CountryVaccines>> mockedMap = Triple.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Mockito.when(this.covidCasesRepository.getAllCases()).thenReturn(mockedMap);
        ResponseEntity<Object> response = this.dataService.calculateCorrelationCoefficient(Optional.empty(), Optional.empty());
        DataResponse dataResponse = (DataResponse) response.getBody();
        Double correlation = dataResponse.getCorrelationCoefficient();

        Assert.assertTrue(dataResponse.getSelectedCountries().isEmpty() && correlation == null);
    }
}