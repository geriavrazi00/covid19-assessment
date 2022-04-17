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

    /**
     * This group of variables is used to store the data retrieved from the API. There 2 sets of maps, the first two for
     * the cases data and the second ones for the vaccination data.
     */
    private Map<String, CountryCases> countryCasesMap = new HashMap<>();
    private Map<String, List<CountryCases>> continentCasesMap = new HashMap<>();
    private Map<String, CountryVaccines> countryVaccinesMap = new HashMap<>();
    private Map<String, List<CountryVaccines>> continentVaccinesMap = new HashMap<>();

    /**
     * This group of variables is used in the retry strategy of the api calling.
     */
    private static final int MAX_RETRIES = 3;
    private int retryCounter = 0;
    private boolean communicationFailed = false;

    /**
     * The method that starts it all. It is executed asynchronously at a fixed schedule of 20 mins, so it reads the data
     * from the external service every 20 mins. The documentation for the API said that the data there itself is updated
     * approximately every hour, so at first I wanted to keep the schedule trigger every 60-70 mins. After implementing the
     * retry strategy though, if all retry attempts fail than 1 hour would pass until the trigger would launch the process
     * again. 
     *
     */
    @Async
    @Scheduled(fixedRateString = "${schedule.interval.in.millis}")
    public void loadData() {
        log.info("ExternalServiceCommunication: Loading data from the external source");

        retryCounter = 0;
        communicationFailed = false;

        this.loadCases();
        this.loadVaccinations();

        log.info("ExternalServiceCommunication: Finished loading data from the external source");
    }

    private void loadCases() {
        log.info("ExternalServiceCommunication: Started loading the cases");

        String result = this.sendRequest(ExternalUrlPathsEnum.CASES_URL.getPath());
        Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> convertedResponse = apiResponseConverter.convertForCountryCases(result);

        countryCasesMap = convertedResponse.getLeft();
        continentCasesMap = convertedResponse.getRight();

        log.info("ExternalServiceCommunication: Finished loading the cases");
    }

    private void loadVaccinations() {
        log.info("ExternalServiceCommunication: Started loading the vaccinations");

        String result = this.sendRequest(ExternalUrlPathsEnum.VACCINES_URL.getPath());
        Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> convertedResponse = apiResponseConverter.convertForCountryVaccines(result);

        countryVaccinesMap = convertedResponse.getLeft();
        continentVaccinesMap = convertedResponse.getRight();

        log.info("ExternalServiceCommunication: Finished loading the vaccinations");
    }

    /**
     * The method send the request to the API based on the url given as param. I have implemented a retry strategy,
     * thus if a connection or read timeout happens, and we cannot access the external API, the method will try
     * to make the call again as many times as the value of the MAX_RETRIES variable. If none of the calls go
     * successfully, an exception is thrown to interrupt the continuation of the flow. When we are present in this case,
     * the boolean communicationFailed will be set to true.
     *
     * @param url
     * @return String
     */
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

    /**
     * We check if the communicationFailed is true. If it is, it throws a ResourceAccessException. This method is called
     * in all the getters above. When outer classes try to get the values of the maps holding the API data, this check
     * will be performed first and the exception will be thrown if it is the case. It is implemented this way to make
     * the outer classes understand that something went wrong when trying to access the data and not just return
     * empty maps.
     */
    private void checkCommunicationStatus() {
        if (communicationFailed) throw new ResourceAccessException("Communication with the external service failed!");
    }
}
