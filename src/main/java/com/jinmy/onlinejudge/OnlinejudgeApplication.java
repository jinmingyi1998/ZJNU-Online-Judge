package com.jinmy.onlinejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlinejudgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlinejudgeApplication.class, args);
    }
}
