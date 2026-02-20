package com.fytzi.apigateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        System.out.println("Starting ApiGatewayApplication... Loading .env from ../");
        Dotenv dotenv = Dotenv.configure()
                .directory("../")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            System.out.println("Loaded environment variable: " + entry.getKey());
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}