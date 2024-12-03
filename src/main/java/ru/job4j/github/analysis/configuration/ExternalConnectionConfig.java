package ru.job4j.github.analysis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExternalConnectionConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
