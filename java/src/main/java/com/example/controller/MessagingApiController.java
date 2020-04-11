package com.example.controller;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.MessagingClient;
import com.example.service.RichMenuService;

import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;
import com.linecorp.bot.model.response.demographics.GetFriendsDemographicsResponse;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessagingApiController {
    private final MessagingClient messagingClient;
    private final RichMenuService richMenuService;

    @PostMapping("/message/push/text")
    public void pushText(@RequestBody @Validated PushText pushText) {
        messagingClient.push(pushText.getTo(), new TextMessage(pushText.getText()));
    }

    @PostMapping("/message/push/sticker")
    public void pushSticker(@RequestBody @Validated PushSticker pushSticker) {
        messagingClient.push(pushSticker.getTo(),
                             new StickerMessage(pushSticker.getPackageId(), pushSticker.getStickerId()));
    }

    @GetMapping("/message/quota")
    public MessageQuotaResponse getMessageQuota() {
        return messagingClient.getMessageQuota();
    }

    @GetMapping("/message/quota/consumption")
    public QuotaConsumptionResponse getMessageQuotaConsumption() {
        return messagingClient.getMessageQuotaConsumption();
    }

    @GetMapping("/message/delivery/reply")
    public NumberOfMessagesResponse getNumberOfSentReplyMessages(@RequestParam String date) {
        return messagingClient.getNumberOfSentReplyMessages(date);
    }

    @GetMapping("/message/delivery/push")
    public NumberOfMessagesResponse getNumberOfSentPushMessages(@RequestParam String date) {
        return messagingClient.getNumberOfSentPushMessages(date);
    }

    @GetMapping("/message/delivery/multicast")
    public NumberOfMessagesResponse getNumberOfSentMulticastMessages(@RequestParam String date) {
        return messagingClient.getNumberOfSentMulticastMessages(date);
    }

    @GetMapping("/message/delivery/broadcast")
    public NumberOfMessagesResponse getNumberOfSentBroadcastMessages(@RequestParam String date) {
        return messagingClient.getNumberOfSentBroadcastMessages(date);
    }

    @GetMapping("/insight/message/delivery")
    public GetNumberOfMessageDeliveriesResponse getNumberOfMessageDeliveries(@RequestParam String date) {
        return messagingClient.getNumberOfMessageDeliveries(date);
    }

    @GetMapping("/insight/followers")
    public GetNumberOfFollowersResponse getNumberOfFollowers(@RequestParam String date) {
        return messagingClient.getNumberOfFollowers(date);
    }

    @GetMapping("/insight/demographic")
    public GetFriendsDemographicsResponse getFriendsDemographics() {
        return messagingClient.getFriendsDemographics();
    }

    @PostMapping("/richmenu")
    public RichMenuIdResponse createRichMenu(@RequestBody @Validated RichMenuRequest request) {
        return richMenuService.create(request.getName(), request.getChatBarText());
    }

    @GetMapping("/richmenu")
    public RichMenuListResponse getRichMenu() {
        return messagingClient.getRichMenuList();
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

    @Data
    public static class RichMenuRequest {
        private String name;
        private String chatBarText;
    }
}
