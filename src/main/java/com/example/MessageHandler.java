package com.example;

import java.io.IOException;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@LineMessageHandler
@AllArgsConstructor
@Slf4j
public class MessageHandler {

    final LineMessagingClient lineMessagingClient;

    final MessageSender messageSender;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        handleTextContent(event.getReplyToken(), event, event.getMessage());
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        StickerMessageContent content = event.getMessage();
        StickerMessage message = new StickerMessage(content.getPackageId(), content.getStickerId());
        messageSender.reply(event.getReplyToken(), message);
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent content = event.getMessage();
        LocationMessage message = new LocationMessage(content.getTitle(),
                                                      content.getAddress(),
                                                      content.getLatitude(),
                                                      content.getLongitude());
        messageSender.reply(event.getReplyToken(), message);
    }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
        log.info("event:{}", event);
    }

    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
        log.info("event:{}", event);
    }

    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) throws IOException {
        log.info("event:{}", event);
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("unfollowed this bot: {}", event);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        messageSender.replyText(event.getReplyToken(), "Got followed event");
    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        messageSender.replyText(event.getReplyToken(), "Joined " + event.getSource());
    }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        messageSender.replyText(event.getReplyToken(), "Got postback " + event.getPostbackContent().getData());
    }

    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) {
        messageSender.replyText(event.getReplyToken(), "Got beacon message " + event.getBeacon().getHwid());
    }

    @EventMapping
    public void handleOtherEvent(Event event) {
        log.info("Received message(Ignored): {}", event);
    }

    public void handleTextContent(String replyToken,
                                  Event event,
                                  TextMessageContent content) {
        String text = content.getText();
        Source source = event.getSource();
        log.info("Got text message from {}: {} {}", replyToken, text, source);

        switch (text) {
            case "bye": {
                if (source instanceof GroupSource) {
                    messageSender.replyText(replyToken, "Leaving group");
                    lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId())
                                       .thenAccept(response -> log.info("response: {}", response));

                } else if (source instanceof RoomSource) {
                    messageSender.replyText(replyToken, "Leaving room");
                    lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId())
                                       .thenAccept(response -> log.info("response: {}", response));
                } else {
                    messageSender.replyText(replyToken, "Bot can't leave from 1:1 chat");
                }
                break;
            }
            default:
                messageSender.replyText(replyToken, text);
                break;
        }
    }
}
