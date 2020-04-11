package com.example.controller;

import com.example.client.MessagingClient;
import com.example.service.MessageEventService;

import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.LeaveEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.FileMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@LineMessageHandler
@RequiredArgsConstructor
@Slf4j
public class EventHandler {
    private final MessageEventService messageEventService;
    private final MessagingClient messagingClient;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        messageEventService.handleTextContent(event.getReplyToken(), event, event.getMessage());
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        final StickerMessageContent content = event.getMessage();
        messagingClient.reply(event.getReplyToken(),
                              new StickerMessage(content.getPackageId(), content.getStickerId()));
    }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        final ImageMessageContent content = event.getMessage();
        log.info("ImageMessageContent:{}", content);
    }

    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) {
        final VideoMessageContent content = event.getMessage();
        log.info("VideoMessageContent:{}", content);
    }

    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) {
        final AudioMessageContent content = event.getMessage();
        log.info("AudioMessageContent:{}", content);
    }

    @EventMapping
    public void handleFileMessageEvent(MessageEvent<FileMessageContent> event) {
        final FileMessageContent content = event.getMessage();
        log.info("FileMessageContent:{}", content);
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        final LocationMessageContent content = event.getMessage();
        messagingClient.reply(event.getReplyToken(),
                              new LocationMessage(content.getTitle(), content.getAddress(),
                                                  content.getLatitude(), content.getLongitude()));
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        messagingClient.replyText(event.getReplyToken(), "Got followed event");
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("unfollowedEvent: {}", event);
    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        messagingClient.replyText(event.getReplyToken(), "Joined " + event.getSource());
    }

    @EventMapping
    public void handleLeaveEvent(LeaveEvent event) {
        log.info("leaveEvent: {}", event);
    }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        final PostbackContent content = event.getPostbackContent();
        final String message = "postback data:" + content.getData() + " params:" + content.getParams();
        messagingClient.replyText(event.getReplyToken(), message);
    }

    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) {
        messagingClient.replyText(event.getReplyToken(),
                                  "Got beacon message " + event.getBeacon().getHwid());
    }

    @EventMapping
    public void handleOtherEvent(Event event) {
        log.info("Received message(Ignored): {}", event);
    }
}
