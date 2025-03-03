package io.nuwe.technical.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@SpringBootApplication
public class APIConfig{

    public static void main(String[] args) {
	SpringApplication.run(APIConfig.class, args);
    }

}
