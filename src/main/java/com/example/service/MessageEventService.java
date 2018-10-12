package com.example.service;

import static com.example.config.Constants.IMAGE_URL;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.config.LiffProperties;
import com.example.config.LiffProperties.LinkUrl;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.richmenu.RichMenuResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageEventService {
    private final LiffProperties liffProperties;
    private final LineMessagingClient lineMessagingClient;
    private final MessageSender messageSender;
    private final QuickReplyService quickReplyService;
    private final FlexMessageService flexMessageService;
    private final TemplateMessageService templateMessageService;
    private final ImagemapMessageService imagemapMessageService;
    private final RichMenuService richMenuService;

    public void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        final String text = content.getText();
        final Source source = event.getSource();
        log.info("Got text message from {}: {} {}", replyToken, text, source);

        switch (text) {
            case "profile": {
                final String userId = event.getSource().getUserId();
                if (userId != null) {
                    lineMessagingClient.getProfile(userId)
                                       .whenComplete((profile, t) -> {
                                           if (t != null) {
                                               log.error(t.getMessage());
                                               return;
                                           }
                                           messageSender.replyText(replyToken, profile.toString());
                                       });
                }
                break;
            }

            case "bye": {
                final String message;
                if (source instanceof GroupSource) {
                    message = "Leaving group";
                    lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId());
                } else if (source instanceof RoomSource) {
                    message = "Leaving room";
                    lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId());
                } else {
                    message = "Bot can't leave from 1:1 chat";
                }
                messageSender.replyText(replyToken, message);
                break;
            }

            case "liff": {
                final LinkUrl linkUrl = liffProperties.getLinkUrl();
                final String url = UriComponentsBuilder.fromHttpUrl(IMAGE_URL)
                                                       .toUriString();
                final ButtonsTemplate template =
                        new ButtonsTemplate(url, "Button Template", "LIFF",
                                            Arrays.asList(new URIAction("compact", linkUrl.getCompact()),
                                                          new URIAction("tall", linkUrl.getTall()),
                                                          new URIAction("full", linkUrl.getFull())));
                messageSender.reply(replyToken, new TemplateMessage("Button Template", template));
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
            case "richmenu":
                final List<RichMenuResponse> richMenuResponses = richMenuService.getList();
                messageSender.replyText(replyToken, richMenuResponses.toString());
                break;
            default:
                break;
        }
    }
}
