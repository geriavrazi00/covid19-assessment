package com.assessment.covid19;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Covid19AssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19AssessmentApplication.class, args);
    }

    /**
     * Creating a bean for the RestTemplate object. Done it to easily configure the connection and read timeout times,
     * as well as to facilitate the unit testing of the classes.
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        final int connectTimeout = 15000;
        final int readTimeout = 10000;

        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(connectTimeout); // millis
        simpleClientHttpRequestFactory.setReadTimeout(readTimeout);  // millis
        return restTemplate;
    }
}
