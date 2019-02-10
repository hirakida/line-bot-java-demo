package com.example.service;

import static com.example.config.Constants.IMAGE_URL;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.stereotype.Service;

import com.example.client.MessageSender;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
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
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlexMessageService {
    private final MessageSender messageSender;

    public void replyMessage(String replyToken) {
        final Image heroBlock = Image.builder()
                                     .url(IMAGE_URL)
                                     .size(ImageSize.FULL_WIDTH)
                                     .aspectRatio(ImageAspectRatio.R20TO13)
                                     .aspectMode(ImageAspectMode.Cover)
                                     .action(new URIAction("label", "https://developers.line.me"))
                                     .build();

        final Box bodyBlock = Box.builder()
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

        final Box footerBlock = Box.builder()
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

        final Bubble bubble = Bubble.builder()
                                    .hero(heroBlock)
                                    .body(bodyBlock)
                                    .footer(footerBlock)
                                    .build();

        messageSender.reply(replyToken, new FlexMessage("Flex Message", bubble));
    }
}
