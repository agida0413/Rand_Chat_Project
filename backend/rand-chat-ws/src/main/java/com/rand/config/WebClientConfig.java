package com.rand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean(name = "webCl")
    @Primary
    public WebClient webClientBuilder() {
        return WebClient.builder().baseUrl("http://nginx:80").build();
    }
}
