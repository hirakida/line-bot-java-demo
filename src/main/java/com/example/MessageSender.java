package com.example;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class MessageSender {

    final LineMessagingClient lineMessagingClient;

    /**
     * reply message
     */
    public void replyText(String replyToken, String message) {
        reply(replyToken, new TextMessage(message));
    }

    public void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        ReplyMessage message = new ReplyMessage(replyToken, messages);
        lineMessagingClient.replyMessage(message)
                           .thenAccept(response -> log.info("response: {}", response));
    }

    /**
     * push message
     */
    public void pushText(String to, String message) {
        push(to, new TextMessage(message));
    }

    public void pushSticker(String to, String packageId, String stickerId) {
        push(to, new StickerMessage(packageId, stickerId));
    }

    public void push(String to, Message message) {
        push(to, Collections.singletonList(message));
    }

    private void push(String to, List<Message> messages) {
        PushMessage pushMessage = new PushMessage(to, messages);
        lineMessagingClient.pushMessage(pushMessage);
    }
}
