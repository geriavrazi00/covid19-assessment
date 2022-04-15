package com.assessment.covid19.services.external;

import com.assessment.covid19.MockGenerator;
import com.assessment.covid19.converters.ApiResponseConverter;
import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.models.enums.ExternalUrlPathsEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ExternalCommunicationServiceTest {
    @Spy
    @InjectMocks
    private ExternalCommunicationService communicationService;

    @Mock
    private ApiResponseConverter apiResponseConverter;

    @Mock
    private RestTemplate restTemplate;

    private Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> casesResponse;
    private Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> vaccinesResponse;

    @Before
    public void init() throws IOException {
        casesResponse = MockGenerator.buildPairOfCountryCases();
        vaccinesResponse = MockGenerator.buildPairOfCountryVaccines();
        String baseUrl = "test";

        ReflectionTestUtils.setField(communicationService, "apiBase", baseUrl);
        Mockito.when(restTemplate.getForObject(baseUrl + ExternalUrlPathsEnum.CASES_URL.getPath(), String.class)).thenReturn("Response from server");
        Mockito.when(restTemplate.getForObject(baseUrl + ExternalUrlPathsEnum.VACCINES_URL.getPath(), String.class)).thenReturn("Response from server");
        Mockito.when(apiResponseConverter.convertForCountryCases(Mockito.any())).thenReturn(casesResponse);
        Mockito.when(apiResponseConverter.convertForCountryVaccines(Mockito.any())).thenReturn(vaccinesResponse);
    }

    @Test
    public void loadData() throws IOException {
        this.communicationService.loadData();
        assertTrue(this.communicationService.getContinentCasesMap() != null
            && this.communicationService.getContinentCasesMap().size() == 1
            && this.communicationService.getContinentCasesMap().equals(casesResponse.getRight())
            && this.communicationService.getCountryCasesMap() != null
            && this.communicationService.getCountryCasesMap().size() == MockGenerator.EUROPEAN_COUNTRIES.length
            && this.communicationService.getCountryCasesMap().equals(casesResponse.getLeft())
            && this.communicationService.getContinentVaccinesMap() != null
            && this.communicationService.getContinentVaccinesMap().size() == 1
            && this.communicationService.getContinentVaccinesMap().equals(vaccinesResponse.getRight())
            && this.communicationService.getCountryVaccinesMap() != null
            && this.communicationService.getCountryVaccinesMap().size() == MockGenerator.EUROPEAN_COUNTRIES.length
            && this.communicationService.getCountryVaccinesMap().equals(vaccinesResponse.getLeft())
        );
    }
}