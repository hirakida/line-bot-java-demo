package com.example.controller;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.MessageSender;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final MessageSender messageSender;

    @PostMapping("/api/text")
    public void pushText(@RequestBody @Validated PushText pushText) {
        messageSender.pushText(pushText.getTo(), pushText.getText());
    }

    @PostMapping("/api/sticker")
    public void pushSticker(@RequestBody @Validated PushSticker pushSticker) {
        messageSender.pushSticker(pushSticker.getTo(),
                                  pushSticker.getPackageId(),
                                  pushSticker.getStickerId());
    }

    @Data
    public static class PushText {
        @NotEmpty
        private String to;
        @NotEmpty
        private String text;
    }

    @Data
    public static class PushSticker {
        @NotEmpty
        private String to;
        @NotEmpty
        private String packageId;
        @NotEmpty
        private String stickerId;
    }
}
