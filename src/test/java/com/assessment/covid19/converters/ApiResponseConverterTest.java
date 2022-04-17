package com.assessment.covid19.converters;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiResponseConverterTest {
    @Spy
    @InjectMocks
    private ApiResponseConverter responseConverter;

    @Test
    public void convertForCountryCases() throws IOException {
        String apiResponse = Files.readString(Path.of("src//main//resources//static//cases"), StandardCharsets.UTF_8);
        Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> result
                = responseConverter.convertForCountryCases(apiResponse);
        assertTrue(!result.getLeft().isEmpty() && !result.getRight().isEmpty());
    }

    @Test
    public void convertForCountryVaccines() throws IOException {
        String apiResponse = Files.readString(Path.of("src//main//resources//static//vaccines"), StandardCharsets.UTF_8);
        Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> result
                = responseConverter.convertForCountryVaccines(apiResponse);
        assertTrue(!result.getLeft().isEmpty() && !result.getRight().isEmpty());
    }

    @Test
    public void convertForCountryCasesWithArrayInput() {
        String apiResponse = "[Simple string response]";
        Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> result
                = responseConverter.convertForCountryVaccines(apiResponse);
        assertTrue(result.getLeft().isEmpty() && result.getRight().isEmpty());
    }

    @Test
    public void convertForCountryCasesWithEmptyInput() {
        String apiResponse = StringUtils.EMPTY;
        Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> result
                = responseConverter.convertForCountryVaccines(apiResponse);
        assertTrue(result.getLeft().isEmpty() && result.getRight().isEmpty());
    }

    @Test
    public void convertForCountryCasesWithNullInput() {
        Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> result
                = responseConverter.convertForCountryVaccines(null);
        assertTrue(result.getLeft().isEmpty() && result.getRight().isEmpty());
    }
}