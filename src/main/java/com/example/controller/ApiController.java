package com.example.controller;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.DeliveryResponse;
import com.example.client.MessagingApiClient;
import com.example.client.MessageSender;
import com.example.service.RichMenuService;

import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final MessageSender messageSender;
    private final MessagingApiClient messagingApiClient;
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

    @GetMapping("/api/delivery/reply")
    public DeliveryResponse getReplyDelivery(@RequestParam String date) {
        return messagingApiClient.getReplyDelivery(date);
    }

    @GetMapping("/api/delivery/push")
    public DeliveryResponse getPushDelivery(@RequestParam String date) {
        return messagingApiClient.getPushDelivery(date);
    }

    @GetMapping("/api/delivery/multicast")
    public DeliveryResponse getMulticastDelivery(@RequestParam String date) {
        return messagingApiClient.getMulticastDelivery(date);
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
