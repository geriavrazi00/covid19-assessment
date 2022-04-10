package com.assessment.covid19.services.external;

import com.assessment.covid19.converters.ApiResponseConverter;
import com.assessment.covid19.models.CountryCases;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    // "C:\\Users\\geria\\IdeaProjects\\covid19-assesment\\src\\main\\resources\\static\\cases"
    private void loadCases() throws IOException {
        log.info("ExternalServiceCommunication: Started loading the cases");

//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(this.apiBase + "/cases", String.class);
        Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> convertedResponse = apiResponseConverter.convertJsonToCountryCases("C:\\Users\\geria\\IdeaProjects\\covid19-assesment\\src\\main\\resources\\static\\cases");

        countryCasesMap = convertedResponse.getLeft();
        continentCasesMap = convertedResponse.getRight();

        log.info("ExternalServiceCommunication: Finished loading the cases");
    }

    private void loadVaccinations() {
        log.info("ExternalServiceCommunication: Started loading the vaccinations");
        log.info("ExternalServiceCommunication: Finished loading the vaccinations");
    }
}
