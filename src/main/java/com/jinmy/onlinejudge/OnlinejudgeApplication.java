package com.jinmy.onlinejudge;

import com.jinmy.onlinejudge.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
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
