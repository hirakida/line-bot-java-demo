package com.example.service;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.config.LiffProperties;
import com.example.config.LiffProperties.LinkUrl;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private static final String IMAGE_URL = "https://avatars1.githubusercontent.com/u/12070156";
    private final LineMessagingClient lineMessagingClient;
    private final MessageSender messageSender;
    private final LiffProperties liffProperties;

    public void handleTextContent(String replyToken, Event event, TextMessageContent content) {
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
                                           messageSender.replyText(replyToken, profile.toString());
                                       });
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
                messageSender.replyText(replyToken, message);
                break;
            }

            case "liff": {
                LinkUrl linkUrl = liffProperties.getLinkUrl();
                ButtonsTemplate template =
                        new ButtonsTemplate(buildImageUrl("1040.jpeg"),
                                            "Button Template",
                                            "LIFF",
                                            Arrays.asList(new URIAction("compact", linkUrl.getCompact()),
                                                          new URIAction("tall", linkUrl.getTall()),
                                                          new URIAction("full", linkUrl.getFull())));
                messageSender.replyButtonsTemplate(replyToken, "Button Template", template);
                break;
            }

            case "imagemap": {
                ImagemapMessage message =
                        new ImagemapMessage(
                                buildImageUrl("imagemap"),
                                "Imagemap Template",
                                new ImagemapBaseSize(1040, 1040),
                                Arrays.asList(new URIImagemapAction("https://line.me",
                                                                    new ImagemapArea(0, 0, 520, 520)),
                                              new URIImagemapAction("https://developers.line.me/ja/",
                                                                    new ImagemapArea(0, 520, 520, 520)),
                                              new MessageImagemapAction("message1",
                                                                        new ImagemapArea(520, 0, 520, 520)),
                                              new MessageImagemapAction("message2",
                                                                        new ImagemapArea(520, 520, 520, 520))));
                messageSender.replyImagemap(replyToken, message);
                break;
            }

            case "flex": {
                Image heroBlock = Image.builder()
                                       .url(IMAGE_URL)
                                       .size(ImageSize.FULL_WIDTH)
                                       .aspectRatio(ImageAspectRatio.R20TO13)
                                       .aspectMode(ImageAspectMode.Cover)
                                       .action(new URIAction("label", "https://developers.line.me"))
                                       .build();

                Box bodyBlock = Box.builder()
                                   .layout(FlexLayout.VERTICAL)
                                   .contents(Arrays.asList(Text.builder()
                                                               .text("text1")
                                                               .weight(TextWeight.BOLD)
                                                               .size(FlexFontSize.XL)
                                                               .build(),
                                                           Box.builder()
                                                              .layout(FlexLayout.BASELINE)
                                                              .margin(FlexMarginSize.MD)
                                                              .contents(Arrays.asList(
                                                                      Icon.builder()
                                                                          .size(FlexFontSize.SM)
                                                                          .url(IMAGE_URL)
                                                                          .build(),
                                                                      Text.builder()
                                                                          .text("text2")
                                                                          .size(FlexFontSize.SM)
                                                                          .color("#999999")
                                                                          .margin(FlexMarginSize.MD)
                                                                          .flex(0)
                                                                          .build()))
                                                              .build(),
                                                           Box.builder()
                                                              .layout(FlexLayout.VERTICAL)
                                                              .margin(FlexMarginSize.LG)
                                                              .spacing(FlexMarginSize.SM)
                                                              .contents(Collections.singletonList(
                                                                      Box.builder()
                                                                         .layout(FlexLayout.BASELINE)
                                                                         .spacing(FlexMarginSize.SM)
                                                                         .contents(Arrays.asList(
                                                                                 Text.builder()
                                                                                     .text("text3")
                                                                                     .flex(1)
                                                                                     .build(),
                                                                                 Text.builder()
                                                                                     .text("text4")
                                                                                     .wrap(true)
                                                                                     .flex(5)
                                                                                     .build()))
                                                                         .build()))
                                                              .build()))
                                   .build();

                Box footerBlock = Box.builder()
                                     .layout(FlexLayout.VERTICAL)
                                     .spacing(FlexMarginSize.SM)
                                     .contents(Arrays.asList(Spacer.builder().build(),
                                                             Button.builder()
                                                                   .style(ButtonStyle.LINK)
                                                                   .height(ButtonHeight.SMALL)
                                                                   .action(new URIAction("LINE",
                                                                                         "https://line.me"))
                                                                   .build(),
                                                             Separator.builder().build(),
                                                             Button.builder()
                                                                   .style(ButtonStyle.LINK)
                                                                   .height(ButtonHeight.SMALL)
                                                                   .action(new URIAction("LINE Developers",
                                                                                         "https://developers.line.me"))
                                                                   .build()))
                                     .build();

                messageSender.replyFlexBubble(replyToken, "Flex", heroBlock, bodyBlock, footerBlock);
                break;
            }

            case "confirm": {
                ConfirmTemplate template = new ConfirmTemplate("Yes or No",
                                                               new MessageAction("Yes", "yes"),
                                                               new MessageAction("No", "no"));
                messageSender.replyConfirmTemplate(replyToken, "Confirm Template", template);
                break;
            }

            case "buttons": {
                ButtonsTemplate template =
                        new ButtonsTemplate(IMAGE_URL,
                                            "Button Template",
                                            "Button Template",
                                            Arrays.asList(new URIAction("uri", "https://line.me"),
                                                          new MessageAction("message", "hello!"),
                                                          new PostbackAction("postback",
                                                                             "postback data",
                                                                             "postback text"),
                                                          new DatetimePickerAction("datetime",
                                                                                   "action=sel",
                                                                                   "datetime")));
                messageSender.replyButtonsTemplate(replyToken, "Button Template", template);
                break;
            }

            case "carousel": {
                CarouselTemplate template =
                        new CarouselTemplate(Arrays.asList(
                                new CarouselColumn(IMAGE_URL,
                                                   "Carousel Template1",
                                                   "URI Action",
                                                   Arrays.asList(
                                                           new URIAction("uri1", "https://line.me"),
                                                           new URIAction("uri2",
                                                                         "https://developers.line.me/ja/"),
                                                           new URIAction("uri3",
                                                                         "https://notify-bot.line.me/ja/"))),
                                new CarouselColumn(IMAGE_URL,
                                                   "Carousel Template2",
                                                   "Message Action",
                                                   Arrays.asList(new MessageAction("message1", "hello!"),
                                                                 new MessageAction("message2", "hello!!"),
                                                                 new MessageAction("message3", "hello!!!"))),
                                new CarouselColumn(IMAGE_URL,
                                                   "Carousel Template3",
                                                   "Postback Action",
                                                   Arrays.asList(new PostbackAction("postback1",
                                                                                    "postback data1",
                                                                                    "postback text1"),
                                                                 new PostbackAction("postback2",
                                                                                    "postback data2",
                                                                                    "postback text2"),
                                                                 new PostbackAction("postback3",
                                                                                    "postback data3",
                                                                                    "postback text3"))),
                                new CarouselColumn(IMAGE_URL,
                                                   "Carousel Template4",
                                                   "Datetime Picker Action",
                                                   Arrays.asList(new DatetimePickerAction("datetime",
                                                                                          "action=sel",
                                                                                          "datetime"),
                                                                 new DatetimePickerAction("date",
                                                                                          "action=sel&only=date",
                                                                                          "date"),
                                                                 new DatetimePickerAction("time",
                                                                                          "action=sel&only=time",
                                                                                          "time")))));
                messageSender.replyCarouselTemplate(replyToken, "Carousel Template", template);
                break;
            }

            case "image_carousel": {
                ImageCarouselTemplate template =
                        new ImageCarouselTemplate(Arrays.asList(
                                new ImageCarouselColumn(IMAGE_URL,
                                                        new URIAction("uri", "https://line.me")),
                                new ImageCarouselColumn(IMAGE_URL,
                                                        new MessageAction("message", "hello")),
                                new ImageCarouselColumn(IMAGE_URL,
                                                        new PostbackAction("postback",
                                                                           "postback data",
                                                                           "postback text"))));
                messageSender.replyImageCarouselTemplate(replyToken, "Image Carousel Template", template);
                break;
            }

            default:
//                messageSender.replyText(replyToken, text);
                break;
        }
    }

    private static String buildImageUrl(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }
}
