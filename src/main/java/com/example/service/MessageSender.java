package com.example.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

    private final LineMessagingClient lineMessagingClient;

    /**
     * Text Message
     */
    public void replyText(String replyToken, String message) {
        reply(replyToken, new TextMessage(message));
    }

    public void pushText(String to, String message) {
        push(to, new TextMessage(message));
    }

    /**
     * Sticker Message
     */
    public void replySticker(String replyToken, String packageId, String stickerId) {
        reply(replyToken, new StickerMessage(packageId, stickerId));
    }

    public void pushSticker(String to, String packageId, String stickerId) {
        push(to, new StickerMessage(packageId, stickerId));
    }

    /**
     * Location Message
     */
    public void replyLocation(String replyToken, String title, String address,
                              double latitude, double longitude) {
        reply(replyToken, new LocationMessage(title, address, latitude, longitude));
    }

    /**
     * Imagemap Message
     */
    public void replyImagemap(String replyToken, ImagemapMessage imagemapMessage) {
        reply(replyToken, imagemapMessage);
    }

    /**
     * Flex Message
     */
    public void replyFlexBubble(String replyToken, String altText, Image hero, Box body, Box footer) {
        Bubble bubble = Bubble.builder()
                              .hero(hero)
                              .body(body)
                              .footer(footer)
                              .build();
        reply(replyToken, new FlexMessage(altText, bubble));
    }

    /**
     * Confirm Template Message
     */
    public void replyConfirmTemplate(String replyToken, String altText, ConfirmTemplate confirmTemplate) {
        reply(replyToken, new TemplateMessage(altText, confirmTemplate));
    }

    /**
     * Buttons Template Message
     */
    public void replyButtonsTemplate(String replyToken, String altText, ButtonsTemplate buttonsTemplate) {
        reply(replyToken, new TemplateMessage(altText, buttonsTemplate));
    }

    /**
     * Carousel Template Message
     */
    public void replyCarouselTemplate(String replyToken, String altText, CarouselTemplate carouselTemplate) {
        reply(replyToken, new TemplateMessage(altText, carouselTemplate));
    }

    /**
     * Image Carousel Template Message
     */
    public void replyImageCarouselTemplate(String replyToken, String altText,
                                           ImageCarouselTemplate imageCarouselTemplate) {
        reply(replyToken, new TemplateMessage(altText, imageCarouselTemplate));
    }

    public void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        ReplyMessage message = new ReplyMessage(replyToken, messages);
        lineMessagingClient.replyMessage(message)
                           .thenAccept(response -> log.info("response: {}", response));
    }

    public void push(String to, Message message) {
        push(to, Collections.singletonList(message));
    }

    private void push(String to, List<Message> messages) {
        PushMessage pushMessage = new PushMessage(to, messages);
        lineMessagingClient.pushMessage(pushMessage);
    }
}
