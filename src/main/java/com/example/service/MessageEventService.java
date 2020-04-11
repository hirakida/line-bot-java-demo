package com.example.service;

import org.springframework.stereotype.Service;

import com.example.client.MessagingClient;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageEventService {
    private final LineMessagingClient lineMessagingClient;
    private final MessagingClient messagingClient;
    private final QuickReplyService quickReplyService;
    private final FlexMessageService flexMessageService;
    private final TemplateMessageService templateMessageService;
    private final ImagemapMessageService imagemapMessageService;

    public void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        String text = content.getText();
        Source source = event.getSource();
        String userId = event.getSource().getUserId();
        log.info("Got text message from {}: {} {}", replyToken, text, source);

        switch (text) {
            case "profile": {
                if (userId != null) {
                    UserProfileResponse profile = messagingClient.getProfile(userId);
                    messagingClient.replyText(replyToken, profile.toString());
                }
                break;
            }

            case "bye": {
                String message;
                if (source instanceof GroupSource) {
                    message = "Leaving group";
                    lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId());
                } else if (source instanceof RoomSource) {
                    message = "Leaving room";
                    lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId());
                } else {
                    message = "Bot can't leave from 1:1 chat";
                }

                messagingClient.replyText(replyToken, message);
                break;
            }

            case "quick":
                quickReplyService.replyQuickReply(replyToken);
                break;
            case "flex":
                flexMessageService.replyMessage(replyToken);
                break;
            case "confirm":
                templateMessageService.replyConfirmTemplate(replyToken);
                break;
            case "buttons":
                templateMessageService.replyButtonsTemplate(replyToken);
                break;
            case "carousel":
                templateMessageService.replyCarouselTemplate(replyToken);
                break;
            case "image_carousel":
                templateMessageService.replyImageCarouselTemplate(replyToken);
                break;
            case "imagemap":
                imagemapMessageService.replyMessage(replyToken);
                break;
            case "richmenu": {
                RichMenuListResponse response = messagingClient.getRichMenuList();
                messagingClient.replyText(replyToken, response.toString());
                break;
            }
            default:
                break;
        }
    }
}
