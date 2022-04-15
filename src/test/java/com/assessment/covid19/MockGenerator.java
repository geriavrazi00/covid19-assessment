package com.assessment.covid19;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class MockGenerator {
    private static final Random random = new Random();

    private MockGenerator() {}

    public static final String[] EUROPEAN_COUNTRIES = {"Italy", "Germany", "Greece", "Finland", "Sweden"};
    public static final String[] ASIAN_COUNTRIES = {"China", "India", "Israel", "Pakistan", "Kazakhstan"};
    public static final String[] NORTH_AMERICAN_COUNTRIES = {"USA", "Canada", "Jamaica", "Mexico", "Guatemala"};
    public static final String[] CONTINENTS = {"Europe", "Asia", "North America"};

    public static CountryCases buildCountryCase(String country, String continent, Long population) {
        CountryCases countryCases = new CountryCases();
        LongStream deathStream = random.longs(1, 0, population);
        Long deaths = deathStream.findFirst().getAsLong();

        countryCases.setCountry(country);
        countryCases.setContinent(continent);
        countryCases.setPopulation(population);
        countryCases.setDeaths(deaths);
        return countryCases;
    }

    public static CountryVaccines buildCountryVaccines(String country, String continent, Long population) {
        LongStream vaxStream = random.longs(1, 0, population);
        Long vax = vaxStream.findFirst().getAsLong();

        CountryVaccines countryVaccines = new CountryVaccines();
        countryVaccines.setCountry(country);
        countryVaccines.setContinent(continent);
        countryVaccines.setPopulation(population);
        countryVaccines.setPeopleVaccinated(vax);
        return countryVaccines;
    }

    public static Triple<List<String>, List<CountryCases>, List<CountryVaccines>> buildTriple(Optional<String[]> countriesParam, Optional<String> continent) {
        String selectedContinent = continent.orElse(CONTINENTS[0]);
        List<String> countries = new ArrayList<>(Arrays.asList(countriesParam.orElse(EUROPEAN_COUNTRIES)));

        List<CountryCases> countryCases = new ArrayList<>();
        List<CountryVaccines> countryVaccines = new ArrayList<>();

        for (String c : countries) {
            Long population = generatePopulationRandom();

            countryCases.add(buildCountryCase(c, selectedContinent, population));
            countryVaccines.add(buildCountryVaccines(c, selectedContinent, population));
        }

        return Triple.of(countries, countryCases, countryVaccines);
    }

    public static Map<String, CountryCases> buildMapOfCountryCases(Optional<String[]> countries) {
        Map<String, CountryCases> casesMap = new HashMap<>();
        String[] defaultCountries = countries.orElse(EUROPEAN_COUNTRIES);

        for (String country: defaultCountries) {
            casesMap.put(country.toLowerCase(), buildCountryCase(country, "test", generatePopulationRandom()));
        }

        return casesMap;
    }

    public static Map<String, CountryVaccines> buildMapOfCountryVaccines(Optional<String[]> countries) {
        Map<String, CountryVaccines> vaccinesMap = new HashMap<>();
        String[] defaultCountries = countries.orElse(EUROPEAN_COUNTRIES);

        for (String country: defaultCountries) {
            vaccinesMap.put(country.toLowerCase(), buildCountryVaccines(country, "test", generatePopulationRandom()));
        }

        return vaccinesMap;
    }

    public static Map<String, List<CountryCases>> buildMapOfContinentCases() {
        Map<String, List<CountryCases>> continents = new HashMap<>();
        List<CountryCases> countryCases = new ArrayList<>();

        for (String country: EUROPEAN_COUNTRIES) {
            countryCases.add(buildCountryCase(country, CONTINENTS[0], generatePopulationRandom()));
        }

        continents.put(CONTINENTS[0].toLowerCase(), countryCases);
        return continents;
    }

    public static Map<String, List<CountryVaccines>> buildMapOfContinentVaccines() {
        Map<String, List<CountryVaccines>> continents = new HashMap<>();
        List<CountryVaccines> countryVaccines = new ArrayList<>();

        for (String country: EUROPEAN_COUNTRIES) {
            countryVaccines.add(buildCountryVaccines(country, CONTINENTS[0], generatePopulationRandom()));
        }

        continents.put(CONTINENTS[0].toLowerCase(), countryVaccines);
        return continents;
    }

    public static Pair<Map<String, CountryCases>, Map<String, List<CountryCases>>> buildPairOfCountryCases() {
        return Pair.of(buildMapOfCountryCases(Optional.empty()), buildMapOfContinentCases());
    }

    public static Pair<Map<String, CountryVaccines>, Map<String, List<CountryVaccines>>> buildPairOfCountryVaccines() {
        return Pair.of(buildMapOfCountryVaccines(Optional.empty()), buildMapOfContinentVaccines());
    }

    private static Long generatePopulationRandom() {
        LongStream populationStream = random.longs(1, 100000, 100000000);
        return populationStream.findFirst().getAsLong();
    }
}
