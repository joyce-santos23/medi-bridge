package br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ViaCepConfig {

    @Value("${integration.viacep.base-url}")
    private String baseUrl;

    @Bean
    public RestClient viaCepRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
