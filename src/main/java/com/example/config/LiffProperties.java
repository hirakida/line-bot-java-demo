package com.example.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "liff", ignoreUnknownFields = false)
@Validated
@Data
public class LiffProperties {

    private LinkUrl linkUrl;

    @Data
    public static class LinkUrl {
        @NotNull
        private String compact;
        @NotNull
        private String tall;
        @NotNull
        private String full;
    }
}
