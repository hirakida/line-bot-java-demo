package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.config.LiffProperties;

@SpringBootApplication
@EnableConfigurationProperties(LiffProperties.class)
public class LineBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LineBotApplication.class, args);
    }
}
