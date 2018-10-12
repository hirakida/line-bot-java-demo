package com.example.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "liff")
@Validated
@Data
public class LiffProperties {
    private LinkUrl linkUrl;

    @Data
    public static class LinkUrl {
        private @NotNull String compact;
        private @NotNull String tall;
        private @NotNull String full;
    }
}
