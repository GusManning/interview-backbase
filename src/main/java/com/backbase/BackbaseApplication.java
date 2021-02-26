package com.backbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.backbase.q1"})
public class BackbaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackbaseApplication.class, args);
    }
}
