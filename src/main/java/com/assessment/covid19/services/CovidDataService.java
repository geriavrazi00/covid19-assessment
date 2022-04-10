package com.assessment.covid19.services;

import com.assessment.covid19.services.external.ExternalCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CovidDataService {

    @Autowired
    private ExternalCommunicationService communicationService;

    public void calculate(Optional<String[]> countries, Optional<String[]> continents) {

    }

}
