package com.backbase;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.backbase.q1", "com.backbase.q2", "com.backbase.q3"})
public class BackbaseApplication {
    public static void main(String[] args) {
    	
    	ConfigurableApplicationContext context = SpringApplication.run(BackbaseApplication.class, args);
    	
    	String[] profiles = context.getEnvironment().getActiveProfiles();
    	System.out.println("ActiveProfiles" + Arrays.toString(profiles) );
    }
}
