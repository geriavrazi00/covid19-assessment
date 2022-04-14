package com.assessment.covid19.repositories;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.services.external.ExternalCommunicationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CovidDataRepositoryTest {
    @Spy
    @InjectMocks
    private CovidDataRepository dataRepository;

    @Mock
    private ExternalCommunicationService communicationService;

    private void initMocks() {
//        Mockito.when(communicationService.getCountryCasesMap()).thenReturn();
//        Mockito.when(communicationService.getCountryVaccinesMap().thenReturn();
    }

    @Test
    public void filterByCountries() {

    }
}