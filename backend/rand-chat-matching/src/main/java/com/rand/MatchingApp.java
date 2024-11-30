package com.rand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatchingApp {
    public static void main(String[] args) {
        //    public static void main(String[] args) {
//        Dotenv dotenv = Dotenv.configure()
//                .directory(System.getProperty("user.dir")) // 루트 디렉토리
//                .load();
//
//        // 환경 변수를 시스템 프로퍼티로 설정
//        System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
//        SpringApplication.run(Application.class, args);
//    }
        SpringApplication.run(MatchingApp.class, args);
    }
}