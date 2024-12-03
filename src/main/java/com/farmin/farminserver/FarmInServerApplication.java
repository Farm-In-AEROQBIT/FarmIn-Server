package com.farmin.farminserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FarmInServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmInServerApplication.class, args);
    }

}
