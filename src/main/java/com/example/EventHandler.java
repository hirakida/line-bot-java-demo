package com.example;

import java.io.IOException;

import com.linecorp.bot.client.LineMessagingClient;
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
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@LineMessageHandler
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final LineMessagingClient lineMessagingClient;
    private final ReplyMessageSender replyMessageSender;

    /**
     * Text Message Event
     */
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        handleTextContent(event.getReplyToken(), event, event.getMessage());
    }

    /**
     * Sticker Message Event
     */
    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        StickerMessageContent content = event.getMessage();
        replyMessageSender.replySticker(event.getReplyToken(), content.getPackageId(), content.getStickerId());
    }

    /**
     * Image Message Event
     */
    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
        ImageMessageContent content = event.getMessage();
        log.info("ImageMessageContent:{}", content);
    }

    /**
     * Video Message Event
     */
    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) throws IOException {
        VideoMessageContent content = event.getMessage();
        log.info("VideoMessageContent:{}", content);
    }

    /**
     * Audio Message Event
     */
    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
        AudioMessageContent content = event.getMessage();
        log.info("AudioMessageContent:{}", content);
    }

    /**
     * File Message Event
     */
    @EventMapping
    public void handleFileMessageEvent(MessageEvent<FileMessageContent> event) throws IOException {
        FileMessageContent content = event.getMessage();
        log.info("FileMessageContent:{}", content);
    }

    /**
     * Location Message Event
     */
    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent content = event.getMessage();
        replyMessageSender.replyLocation(event.getReplyToken(), content.getTitle(), content.getAddress(),
                                         content.getLatitude(), content.getLongitude());
    }

    /**
     * Follow Event
     */
    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        replyMessageSender.replyText(event.getReplyToken(), "Got followed event");
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
        replyMessageSender.replyText(event.getReplyToken(), "Joined " + event.getSource());
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
        replyMessageSender.replyText(event.getReplyToken(),
                                     "Got postback " + event.getPostbackContent().getData());
    }

    /**
     * Beacon Event
     */
    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) {
        replyMessageSender.replyText(event.getReplyToken(),
                                     "Got beacon message " + event.getBeacon().getHwid());
    }

    /**
     * Other Event
     */
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
            case "profile": {
                String userId = event.getSource().getUserId();
                if (userId != null) {
                    lineMessagingClient.getProfile(userId)
                            .whenComplete((profile, t) -> {
                                if (t != null) {
                                    log.error(t.getMessage());
                                    return;
                                }
                                replyMessageSender.replyText(replyToken, profile.toString());
                            });
                }
                break;
            }

            case "bye": {
                if (source instanceof GroupSource) {
                    replyMessageSender.replyText(replyToken, "Leaving group");
                    lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId())
                                       .thenAccept(response -> log.info("response: {}", response));

                } else if (source instanceof RoomSource) {
                    replyMessageSender.replyText(replyToken, "Leaving room");
                    lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId())
                                       .thenAccept(response -> log.info("response: {}", response));
                } else {
                    replyMessageSender.replyText(replyToken, "Bot can't leave from 1:1 chat");
                }
                break;
            }

            case "confirm":
                replyMessageSender.replyConfirmTemplate(replyToken);
                break;

            case "buttons":
                replyMessageSender.replyButtonsTemplate(replyToken);
                break;

            case "carousel":
                replyMessageSender.replyCarouselTemplate(replyToken);
                break;

            case "image_carousel":
                replyMessageSender.replyImageCarouselTemplate(replyToken);
                break;

            case "imagemap":
                replyMessageSender.replyImagemapTemplate(replyToken);
                break;

            default:
                replyMessageSender.replyText(replyToken, text);
                break;
        }
    }
}
