package com.group1.FresherAcademyManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class FresherAcademyManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(FresherAcademyManagementSystemApplication.class, args);

    }
}
