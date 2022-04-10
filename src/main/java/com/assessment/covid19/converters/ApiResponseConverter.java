package com.assessment.covid19.converters;

import com.assessment.covid19.models.CountryCases;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ApiResponseConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> convertJsonToCountryCases(String apiResponse) throws IOException {
        Map<String, CountryCases> countryCasesTemp = new HashMap<>();
        Map<String, List<CountryCases>> continentCasesTemp = new HashMap<>();

        JsonFactory f = new MappingJsonFactory();
        try (JsonParser jp = f.createParser(apiResponse)) {
            if (jp.nextToken() != JsonToken.START_OBJECT) {
                log.error("Error in convertJsonToCountryCases(): The JSON format is wrongly formatted.");
                return Pair.of(countryCasesTemp, continentCasesTemp);
            }

            JsonToken current = jp.nextToken();

            while (current != JsonToken.END_OBJECT) {
                String countryName = jp.getCurrentName();

                if (jp.nextToken() != JsonToken.END_OBJECT) {
                    jp.nextToken();

                    if (jp.getCurrentName().equals("All")) {
                        jp.nextToken();
                        CountryCases countryCases = objectMapper.readValue(jp.readValueAsTree().toString(), CountryCases.class);
                        if (countryCases.getCountry() == null) countryCases.setCountry(countryName);
                        countryCasesTemp.put(countryName, countryCases);

                        if (!continentCasesTemp.containsKey(countryCases.getContinent())) {
                            continentCasesTemp.put(countryCases.getContinent(), new ArrayList<>());
                        }
                        continentCasesTemp.get(countryCases.getContinent()).add(countryCases);
                    } else {
                        jp.nextToken();
                        jp.readValueAsTree();
                    }
                    jp.nextToken();
                    current = jp.nextToken();
                }
            }
        }

        return Pair.of(countryCasesTemp, continentCasesTemp);
    }
}
