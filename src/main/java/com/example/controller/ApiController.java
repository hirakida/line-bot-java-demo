package com.example.controller;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.MessageSender;
import com.example.service.RichMenuService;

import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final MessageSender messageSender;
    private final RichMenuService richMenuService;

    @PostMapping("/api/text")
    public void pushText(@RequestBody @Validated PushText pushText) {
        messageSender.push(pushText.getTo(), new TextMessage(pushText.getText()));
    }

    @PostMapping("/api/sticker")
    public void pushSticker(@RequestBody @Validated PushSticker pushSticker) {
        messageSender.push(pushSticker.getTo(),
                           new StickerMessage(pushSticker.getPackageId(), pushSticker.getStickerId()));
    }

    @PostMapping("/api/richmenu")
    public RichMenuResponse createRichMenu(@RequestBody @Validated RichMenuRequest request) {
        final String richMenuId = richMenuService.create(request.getName(), request.getChatBarText());
        return new RichMenuResponse(richMenuId);
    }

    @GetMapping("/api/richmenu")
    public void getRichMenu() {
        richMenuService.getList();
    }

    @Data
    public static class PushText {
        private @NotEmpty String to;
        private @NotEmpty String text;
    }

    @Data
    public static class PushSticker {
        private @NotEmpty String to;
        private @NotEmpty String packageId;
        private @NotEmpty String stickerId;
    }

    @Data
    public static class RichMenuRequest {
        private String name;
        private String chatBarText;
    }

    @Data
    public static class RichMenuResponse {
        private final String richMenuId;
    }
}
