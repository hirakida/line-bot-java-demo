package com.example.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSender {
    private final LineMessagingClient lineMessagingClient;

    public void replyText(String replyToken, String message) {
        reply(replyToken, new TextMessage(message));
    }

    public void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        final ReplyMessage message = new ReplyMessage(replyToken, messages);
        lineMessagingClient.replyMessage(message)
                           .thenAccept(response -> log.info("response: {}", response));
    }

    public void push(String to, Message message) {
        push(to, Collections.singletonList(message));
    }

    private void push(String to, List<Message> messages) {
        final PushMessage pushMessage = new PushMessage(to, messages);
        lineMessagingClient.pushMessage(pushMessage);
    }
}
