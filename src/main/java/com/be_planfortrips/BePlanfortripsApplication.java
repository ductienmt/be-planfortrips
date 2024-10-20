package com.be_planfortrips;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BePlanfortripsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BePlanfortripsApplication.class, args);
    }

}
