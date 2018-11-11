package com.hpe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HpeApplication {

    public static void main(String... args) {
        SpringApplication.run(HpeApplication.class, args);
    }
}
