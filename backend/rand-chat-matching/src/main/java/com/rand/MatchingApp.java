package com.rand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatchingApp {
    public static void main(String[] args) {
        SpringApplication.run(MatchingApp.class, args);
    }
}