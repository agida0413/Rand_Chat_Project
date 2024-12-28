package com.rand.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("web.nginx.url")
    private String url;

    @Bean(name = "webCl")
    @Primary
    public WebClient webClientBuilder() {
        return WebClient.builder().baseUrl(url).build();
    }
}
