package com.assessment.covid19.services.external;

import com.assessment.covid19.converters.ApiResponseConverter;
import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Getter
@Slf4j
public class ExternalCommunicationService {
    @Value("${external.api.base}")
    private String apiBase;

    @Autowired
    private ApiResponseConverter apiResponseConverter;

    private Map<String, CountryCases> countryCasesMap = new HashMap<>();
    private Map<String, List<CountryCases>> continentCasesMap = new HashMap<>();
    private Map<String, CountryVaccines> countryVaccinesMap = new HashMap<>();
    private Map<String, List<CountryVaccines>> continentVaccinesMap = new HashMap<>();

    // Call the method every 70 min since the api itself loads the data approximately every 60 min
    @Async
    @Scheduled(fixedRateString = "${schedule.interval.in.millis}")
//    @Scheduled(fixedRate = 500)
    public void loadData() throws IOException {
        log.info("ExternalServiceCommunication: Loading data from the external source");

        this.loadCases();
        this.loadVaccinations();

        log.info("ExternalServiceCommunication: Finished loading data from the external source");
    }

    private void loadCases() throws IOException {
        log.info("ExternalServiceCommunication: Started loading the cases");

        String result = this.sendRequest("/cases");
        Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> convertedResponse = apiResponseConverter.convertForCountryCases(result);

        countryCasesMap = convertedResponse.getLeft();
        continentCasesMap = convertedResponse.getRight();

        log.info("ExternalServiceCommunication: Finished loading the cases");
    }

    private void loadVaccinations() throws IOException {
        log.info("ExternalServiceCommunication: Started loading the vaccinations");

        String result = this.sendRequest("/vaccines");
        Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> convertedResponse = apiResponseConverter.convertForCountryVaccines(result);

        countryVaccinesMap = convertedResponse.getLeft();
        continentVaccinesMap = convertedResponse.getRight();

        log.info("ExternalServiceCommunication: Finished loading the vaccinations");
    }

    private String sendRequest(String url) {
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate.getForObject(this.apiBase + url, String.class);
        if (url.equals("/cases")) {
            return "C:\\Users\\geria\\IdeaProjects\\covid19-assesment\\src\\main\\resources\\static\\cases";
        } else {
            return "C:\\Users\\geria\\IdeaProjects\\covid19-assesment\\src\\main\\resources\\static\\vaccines";
        }
    }
}
