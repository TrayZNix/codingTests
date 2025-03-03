package io.nuwe.technical.api;

import org.springframework.context.annotation.Configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Configuration
@SpringBootApplication
public class APIConfig{

    public static void main(String[] args) {
	SpringApplication.run(APIConfig.class, args);
    }
}
