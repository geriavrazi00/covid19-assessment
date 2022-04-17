package com.assessment.covid19.services.external;

import com.assessment.covid19.converters.ApiResponseConverter;
import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.assessment.covid19.models.enums.ExternalUrlPathsEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class ExternalCommunicationService {
    @Value("${external.api.base}")
    private String apiBase;

    @Autowired
    private ApiResponseConverter apiResponseConverter;

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, CountryCases> countryCasesMap = new HashMap<>();
    private Map<String, List<CountryCases>> continentCasesMap = new HashMap<>();
    private Map<String, CountryVaccines> countryVaccinesMap = new HashMap<>();
    private Map<String, List<CountryVaccines>> continentVaccinesMap = new HashMap<>();

    private static final int MAX_RETRIES = 3;
    private int retryCounter = 0;
    private boolean communicationFailed = false;

    // Call the method every 70 min since the api itself loads the data approximately every 60 min
    @Async
    @Scheduled(fixedRateString = "${schedule.interval.in.millis}")
//    @Scheduled(fixedRate = 500)
    public void loadData() throws IOException {
        log.info("ExternalServiceCommunication: Loading data from the external source");

        retryCounter = 0;
        communicationFailed = false;

        this.loadCases();
        this.loadVaccinations();

        log.info("ExternalServiceCommunication: Finished loading data from the external source");
    }

    private void loadCases() throws IOException {
        log.info("ExternalServiceCommunication: Started loading the cases");

        String result = this.sendRequest(ExternalUrlPathsEnum.CASES_URL.getPath());
        Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> convertedResponse = apiResponseConverter.convertForCountryCases(result);

        countryCasesMap = convertedResponse.getLeft();
        continentCasesMap = convertedResponse.getRight();

        log.info("ExternalServiceCommunication: Finished loading the cases");
    }

    private void loadVaccinations() throws IOException {
        log.info("ExternalServiceCommunication: Started loading the vaccinations");

        String result = this.sendRequest(ExternalUrlPathsEnum.VACCINES_URL.getPath());
        Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> convertedResponse = apiResponseConverter.convertForCountryVaccines(result);

        countryVaccinesMap = convertedResponse.getLeft();
        continentVaccinesMap = convertedResponse.getRight();

        log.info("ExternalServiceCommunication: Finished loading the vaccinations");
    }

    private String sendRequest(String url) {
        do {
            try {
                return restTemplate.getForObject(this.apiBase + url, String.class);
            } catch (ResourceAccessException exception) {
                communicationFailed = true;
                retryCounter++;
                log.error("Failed sending request to {}. Retry number {}", this.apiBase + url, retryCounter);
            }
        } while (communicationFailed && retryCounter <= MAX_RETRIES);

        throw new ResourceAccessException("Communication with the external service failed!");
    }

    public Map<String, CountryCases> getCountryCasesMap() {
        this.checkCommunicationStatus();
        return this.countryCasesMap;
    }

    public Map<String, List<CountryCases>> getContinentCasesMap() {
        this.checkCommunicationStatus();
        return continentCasesMap;
    }

    public Map<String, CountryVaccines> getCountryVaccinesMap() {
        this.checkCommunicationStatus();
        return countryVaccinesMap;
    }

    public Map<String, List<CountryVaccines>> getContinentVaccinesMap() {
        this.checkCommunicationStatus();
        return continentVaccinesMap;
    }

    private void checkCommunicationStatus() {
        if (communicationFailed) throw new ResourceAccessException("Communication with the external service failed!");
    }
}
