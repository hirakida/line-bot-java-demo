package com.example;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushMessageSender {

    private final LineMessagingClient lineMessagingClient;

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
