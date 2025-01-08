package com.rand.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // cors config
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
           .allowedOrigins("http://localhost:3000","http://randchat.o-r.kr","https://randchat.o-r.kr");//분리 작업시 3000포트 허용

    }
}