package com.assessment.covid19.converters;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ApiResponseConverter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String GLOBAL_COUNTRY_NAME = "Global";

    public Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> convertForCountryCases(String apiResponse) {
        return (Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>>)(Object) convertJson(apiResponse, CountryCases.class.getName(), new HashMap<>(), new HashMap<>());
    }

    public Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> convertForCountryVaccines(String apiResponse) {
        return (Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>>)(Object) convertJson(apiResponse, CountryVaccines.class.getName(), new HashMap<>(), new HashMap<>());
    }

    private void convertForCountryCases(JsonParser jp, String countryName, Map<String, Object> countryCasesTemp, Map<String, List<Object>> continentCasesTemp) throws IOException {
        CountryCases countryCases = objectMapper.readValue(jp.readValueAsTree().toString(), CountryCases.class);
        if (!countryName.equals(GLOBAL_COUNTRY_NAME.toLowerCase())) {
            if (countryCases.getCountry() == null) countryCases.setCountry(countryName);
            countryCasesTemp.put(countryName.toLowerCase(), countryCases);

            String continentName = countryCases.getContinent() != null ? countryCases.getContinent().toLowerCase() : null;
            continentCasesTemp.putIfAbsent(continentName, new ArrayList<>());
            continentCasesTemp.get(continentName).add(countryCases);
        }
    }

    private void convertForCountryVaccines(JsonParser jp, String countryName, Map<String, Object> countryVaccinesTemp, Map<String, List<Object>> continentVaccinesTemp) throws IOException {
        CountryVaccines countryVaccines = objectMapper.readValue(jp.readValueAsTree().toString(), CountryVaccines.class);
        if (!countryName.equals(GLOBAL_COUNTRY_NAME.toLowerCase())) {
            if (countryVaccines.getCountry() == null) countryVaccines.setCountry(countryName);

            countryVaccinesTemp.put(countryName.toLowerCase(), countryVaccines);

            String continentName = countryVaccines.getContinent() != null ? countryVaccines.getContinent().toLowerCase() : null;
            continentVaccinesTemp.putIfAbsent(continentName, new ArrayList<>());
            continentVaccinesTemp.get(continentName).add(countryVaccines);
        }
    }

    /**
     * Converting the API result to a proper Pair of maps of data.
     *
     * @param apiResponse
     * @param classType
     * @param countryCasesTemp
     * @param continentCasesTemp
     * @return
     */
    private Pair<Map<String, Object>, Map<String, List<Object>>> convertJson(String apiResponse, String classType,
            Map<String, Object> countryCasesTemp, Map<String, List<Object>> continentCasesTemp) {
        log.info("ApiResponseConverter: Started converting the JSON");
        JsonFactory f = new MappingJsonFactory();

        try (JsonParser jp = f.createParser(apiResponse)) {
            if (jp.nextToken() != JsonToken.START_OBJECT) {
                log.error("Error in convertJson(): The JSON provided does does not start with an object");
                return Pair.of(countryCasesTemp, continentCasesTemp);
            }

            JsonToken current = jp.nextToken();

            while (current != JsonToken.END_OBJECT) {
                String countryName = jp.getCurrentName().toLowerCase();

                if (jp.nextToken() != JsonToken.END_OBJECT) {
                    jp.nextToken();

                    // We are interested in the "All" object, the other objects inside a country object will be skipped
                    if (jp.getCurrentName().equals("All")) {
                        jp.nextToken();
                        if (classType.equals(CountryCases.class.getName())) {
                            this.convertForCountryCases(jp, countryName, countryCasesTemp, continentCasesTemp);
                        } else if (classType.equals(CountryVaccines.class.getName())) {
                            this.convertForCountryVaccines(jp, countryName, countryCasesTemp, continentCasesTemp);
                        }
                    } else {
                        jp.nextToken();
                        jp.readValueAsTree();
                    }
                    jp.nextToken();
                    current = jp.nextToken();
                }
            }
        } catch (Exception exception) {
            log.error("Error in convertJson(): The JSON is wrongly formatted!");
            return Pair.of(new HashMap<>(), new HashMap<>());
        }

        log.info("ApiResponseConverter: Finished converting the JSON");
        return Pair.of(countryCasesTemp, continentCasesTemp);
    }
}
