package com.assessment.covid19;

import com.assessment.covid19.models.CountryCases;
import com.assessment.covid19.models.CountryVaccines;
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
            LongStream populationStream = random.longs(1, 100000, 100000000);
            Long population = populationStream.findFirst().getAsLong();

            countryCases.add(buildCountryCase(c, selectedContinent, population));
            countryVaccines.add(buildCountryVaccines(c, selectedContinent, population));
        }

        return Triple.of(countries, countryCases, countryVaccines);
    }
}
