package com.example.service;

import static com.example.config.Constants.IMAGE_URL;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.client.MessagingClient;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateMessageService {
    private final MessagingClient messagingClient;

    public void replyConfirmTemplate(String replyToken) {
        final ConfirmTemplate template = new ConfirmTemplate("Yes or No",
                                                             new MessageAction("Yes", "yes"),
                                                             new MessageAction("No", "no"));
        messagingClient.reply(replyToken, new TemplateMessage("Confirm Template", template));
    }

    public void replyButtonsTemplate(String replyToken) {
        List<Action> actions = List.of(new URIAction("uri", URI.create("https://line.me"), null),
                                       new MessageAction("message", "hello!"),
                                       new PostbackAction("postback", "postback data", "postback text"),
                                       DatetimePickerAction.OfLocalDatetime.builder()
                                                                           .label("datetime")
                                                                           .data("data")
                                                                           .build());
        ButtonsTemplate template =
                new ButtonsTemplate(URI.create(IMAGE_URL), "Button Template", "Button Template", actions);

        messagingClient.reply(replyToken, new TemplateMessage("Button Template", template));
    }

    public void replyCarouselTemplate(String replyToken) {
        List<Action> actions1 = List.of(new URIAction("uri1", URI.create("https://line.me"), null),
                                        new URIAction("uri2", URI.create("https://developers.line.me/ja/"),
                                                      null),
                                        new URIAction("uri3", URI.create("https://notify-bot.line.me/ja/"),
                                                      null));
        List<Action> actions2 = List.of(new MessageAction("message1", "hello!"),
                                        new MessageAction("message2", "hello!!"),
                                        new MessageAction("message3", "hello!!!"));
        List<Action> actions3 = List.of(new PostbackAction("postback1", "postback data1", "postback text1"),
                                        new PostbackAction("postback2", "postback data2", "postback text2"),
                                        new PostbackAction("postback3", "postback data3", "postback text3"));
        List<Action> actions4 = List.of(DatetimePickerAction.OfLocalDatetime.builder()
                                                                            .label("datetime")
                                                                            .data("data")
                                                                            .build(),
                                        DatetimePickerAction.OfLocalDate.builder()
                                                                        .label("date")
                                                                        .data("data")
                                                                        .build(),
                                        DatetimePickerAction.OfLocalTime.builder()
                                                                        .label("time")
                                                                        .data("data")
                                                                        .build());
        List<CarouselColumn> columns = List.of(new CarouselColumn(URI.create(IMAGE_URL),
                                                                  "Carousel Template1",
                                                                  "URI Action",
                                                                  actions1),
                                               new CarouselColumn(URI.create(IMAGE_URL),
                                                                  "Carousel Template2",
                                                                  "Message Action",
                                                                  actions2),
                                               new CarouselColumn(URI.create(IMAGE_URL),
                                                                  "Carousel Template3",
                                                                  "Postback Action",
                                                                  actions3),
                                               new CarouselColumn(URI.create(IMAGE_URL),
                                                                  "Carousel Template4",
                                                                  "Datetime Picker Action",
                                                                  actions4));
        final CarouselTemplate template = new CarouselTemplate(columns);

        messagingClient.reply(replyToken, new TemplateMessage("Carousel Template", template));
    }

    public void replyImageCarouselTemplate(String replyToken) {
        List<ImageCarouselColumn> columns = List.of(
                new ImageCarouselColumn(URI.create(IMAGE_URL),
                                        new URIAction("uri", URI.create("https://line.me"), null)),
                new ImageCarouselColumn(URI.create(IMAGE_URL),
                                        new MessageAction("message", "hello")),
                new ImageCarouselColumn(URI.create(IMAGE_URL),
                                        new PostbackAction("postback", "postback data", "postback text")));
        final ImageCarouselTemplate template = new ImageCarouselTemplate(columns);

        messagingClient.reply(replyToken, new TemplateMessage("Image Carousel Template", template));
    }
}
