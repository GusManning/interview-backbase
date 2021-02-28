package com.backbase;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.backbase.q1", "com.backbase.q2", "com.backbase.q3"})
public class BackbaseApplication {
    public static void main(String[] args) {
    	
    	SpringApplication.run(BackbaseApplication.class, args);
    	

    }
}
