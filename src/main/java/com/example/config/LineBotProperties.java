package com.example.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "line.bot")
@Validated
@Data
public class LineBotProperties {
    @NotNull
    private String channelSecret;
    @NotNull
    private String channelToken;
}
