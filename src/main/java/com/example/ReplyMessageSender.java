package com.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class ReplyMessageSender {

    final LineMessagingClient lineMessagingClient;

    /**
     * Text Message
     */
    public void replyText(String replyToken, String message) {
        reply(replyToken, new TextMessage(message));
    }

    /**
     * Sticker Message
     */
    public void replySticker(String replyToken, String packageId, String stickerId) {
        StickerMessage message = new StickerMessage(packageId, stickerId);
        reply(replyToken, message);
    }

    /**
     * Location Message
     */
    public void replyLocation(String replyToken, String title, String address,
                              double latitude, double longitude) {
        LocationMessage message = new LocationMessage(title, address, latitude, longitude);
        reply(replyToken, message);
    }

    /**
     * Confirm Template Message
     */
    public void replyConfirmTemplate(String replyToken) {
        ConfirmTemplate template = new ConfirmTemplate("Are you sure?",
                                                       new MessageAction("OK", "ok"),
                                                       new MessageAction("Cancel", "cancel"));
        reply(replyToken, new TemplateMessage("Confirm Template", template));
    }

    /**
     * Buttons Template Message
     */
    public void replyButtonsTemplate(String replyToken) {
        ButtonsTemplate template = new ButtonsTemplate(
                buildImageUrl("1040.jpeg"),
                "Button Template",
                "text",
                Arrays.asList(new URIAction("uri", "https://line.me"),
                              new PostbackAction("postback", "postback data", "postback text"),
                              new MessageAction("message", "hello")));
        reply(replyToken, new TemplateMessage("Button Template", template));
    }

    /**
     * Carousel Template Message
     */
    public void replyCarouselTemplate(String replyToken) {
        String imageUrl = buildImageUrl("1040.jpeg");
        CarouselTemplate template = new CarouselTemplate(
                Arrays.asList(
                        new CarouselColumn(imageUrl,
                                           "Carousel Template1",
                                           "text1",
                                           Arrays.asList(
                                                   new URIAction("uri1", "https://line.me"),
                                                   new URIAction("uri2", "https://developers.line.me/ja/"),
                                                   new URIAction("uri3", "https://notify-bot.line.me/ja/"))),
                        new CarouselColumn(imageUrl,
                                           "Carousel Template2",
                                           "text2",
                                           Arrays.asList(new PostbackAction("postback", "hello"),
                                                         new MessageAction("message1", "hello1"),
                                                         new MessageAction("message2", "hello2"))),
                        new CarouselColumn(imageUrl,
                                           "Datetime Picker",
                                           "text3",
                                           Arrays.asList(new DatetimePickerAction("datetime",
                                                                                  "action=sel",
                                                                                  "datetime"),
                                                         new DatetimePickerAction("date",
                                                                                  "action=sel&only=date",
                                                                                  "date"),
                                                         new DatetimePickerAction("time",
                                                                                  "action=sel&only=time",
                                                                                  "time")
                                           ))));
        reply(replyToken, new TemplateMessage("Carousel Template", template));
    }

    /**
     * Image Carousel Template Message
     */
    public void replyImageCarouselTemplate(String replyToken) {
        String imageUrl = buildImageUrl("1040.jpeg");
        ImageCarouselTemplate template = new ImageCarouselTemplate(
                Arrays.asList(new ImageCarouselColumn(imageUrl,
                                                      new URIAction("uri", "https://line.me")),
                              new ImageCarouselColumn(imageUrl,
                                                      new MessageAction("message", "hello")),
                              new ImageCarouselColumn(imageUrl,
                                                      new PostbackAction("postback",
                                                                         "hello data",
                                                                         "hello text"))));
        reply(replyToken, new TemplateMessage("Image Carousel Template", template));
    }

    /**
     * Imagemap Template Message
     */
    public void replyImagemapTemplate(String replyToken) {
        ImagemapMessage message =
                new ImagemapMessage(buildImageUrl("imagemap"),
                                    "Imagemap Template",
                                    new ImagemapBaseSize(1040, 1040),
                                    Arrays.asList(new URIImagemapAction(
                                                          "https://line.me",
                                                          new ImagemapArea(0, 0, 520, 520)),
                                                  new URIImagemapAction(
                                                          "https://developers.line.me",
                                                          new ImagemapArea(520, 0, 520, 520)),
                                                  new MessageImagemapAction(
                                                          "hello!",
                                                          new ImagemapArea(0, 520, 520, 520)),
                                                  new MessageImagemapAction(
                                                          "good bye!",
                                                          new ImagemapArea(520, 520, 520, 520))));
        reply(replyToken, message);
    }

    public void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        ReplyMessage message = new ReplyMessage(replyToken, messages);
        lineMessagingClient.replyMessage(message)
                           .thenAccept(response -> log.info("response: {}", response));
    }

    private static String buildImageUrl(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }
}
