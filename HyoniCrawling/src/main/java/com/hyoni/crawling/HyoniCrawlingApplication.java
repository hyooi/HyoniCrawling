package com.hyoni.crawling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAutoConfiguration
public class HyoniCrawlingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HyoniCrawlingApplication.class, args);
	}
}
