package com.example.controller;

import com.example.service.MessageSender;
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
    private final MessageSender messageSender;

    /**
     * Text Message Event
     */
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        messageEventService.handleTextContent(event.getReplyToken(), event, event.getMessage());
    }

    /**
     * Sticker Message Event
     */
    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        final StickerMessageContent content = event.getMessage();
        messageSender.reply(event.getReplyToken(),
                            new StickerMessage(content.getPackageId(), content.getStickerId()));
    }

    /**
     * Image Message Event
     */
    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        final ImageMessageContent content = event.getMessage();
        log.info("ImageMessageContent:{}", content);
    }

    /**
     * Video Message Event
     */
    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) {
        final VideoMessageContent content = event.getMessage();
        log.info("VideoMessageContent:{}", content);
    }

    /**
     * Audio Message Event
     */
    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) {
        final AudioMessageContent content = event.getMessage();
        log.info("AudioMessageContent:{}", content);
    }

    /**
     * File Message Event
     */
    @EventMapping
    public void handleFileMessageEvent(MessageEvent<FileMessageContent> event) {
        final FileMessageContent content = event.getMessage();
        log.info("FileMessageContent:{}", content);
    }

    /**
     * Location Message Event
     */
    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        final LocationMessageContent content = event.getMessage();
        messageSender.reply(event.getReplyToken(),
                            new LocationMessage(content.getTitle(), content.getAddress(),
                                                content.getLatitude(), content.getLongitude()));
    }

    /**
     * Follow Event
     */
    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        messageSender.replyText(event.getReplyToken(), "Got followed event");
    }

    /**
     * UnFollow Event
     */
    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("unfollowedEvent: {}", event);
    }

    /**
     * Join Event
     */
    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        messageSender.replyText(event.getReplyToken(), "Joined " + event.getSource());
    }

    /**
     * Leave Event
     */
    @EventMapping
    public void handleLeaveEvent(LeaveEvent event) {
        log.info("leaveEvent: {}", event);
    }

    /**
     * Postback Event
     */
    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        final PostbackContent content = event.getPostbackContent();
        final String message = "postback data:" + content.getData() + " params:" + content.getParams();
        messageSender.replyText(event.getReplyToken(), message);
    }

    /**
     * Beacon Event
     */
    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) {
        messageSender.replyText(event.getReplyToken(),
                                "Got beacon message " + event.getBeacon().getHwid());
    }

    /**
     * Other Event
     */
    @EventMapping
    public void handleOtherEvent(Event event) {
        log.info("Received message(Ignored): {}", event);
    }
}
