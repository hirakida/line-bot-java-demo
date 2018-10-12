package com.example.service;

import static com.example.config.Constants.IMAGE_URL;

import java.util.Arrays;

import org.springframework.stereotype.Service;

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
    private final MessageSender messageSender;

    public void replyConfirmTemplate(String replyToken) {
        final ConfirmTemplate template = new ConfirmTemplate("Yes or No",
                                                             new MessageAction("Yes", "yes"),
                                                             new MessageAction("No", "no"));
        messageSender.reply(replyToken, new TemplateMessage("Confirm Template", template));
    }

    public void replyButtonsTemplate(String replyToken) {
        final ButtonsTemplate template =
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
        messageSender.reply(replyToken, new TemplateMessage("Button Template", template));
    }

    public void replyCarouselTemplate(String replyToken) {
        final CarouselTemplate template =
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
        messageSender.reply(replyToken, new TemplateMessage("Carousel Template", template));
    }

    public void replyImageCarouselTemplate(String replyToken) {
        final ImageCarouselTemplate template =
                new ImageCarouselTemplate(Arrays.asList(new ImageCarouselColumn(IMAGE_URL,
                                                                                new URIAction("uri",
                                                                                              "https://line.me")),
                                                        new ImageCarouselColumn(IMAGE_URL,
                                                                                new MessageAction("message",
                                                                                                  "hello")),
                                                        new ImageCarouselColumn(IMAGE_URL,
                                                                                new PostbackAction("postback",
                                                                                                   "postback data",
                                                                                                   "postback text"))));
        messageSender.reply(replyToken, new TemplateMessage("Image Carousel Template", template));
    }
}
