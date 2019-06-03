package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.client.MessageSender;

import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImagemapMessageService {
    private final MessageSender messageSender;

    public void replyMessage(String replyToken) {
        final ImagemapMessage message =
                new ImagemapMessage(buildImageUrl("imagemap"),
                                    "Imagemap Template",
                                    new ImagemapBaseSize(1040, 1040),
                                    List.of(new URIImagemapAction("https://line.me",
                                                                  new ImagemapArea(0, 0, 520, 520)),
                                            new URIImagemapAction("https://developers.line.me/ja/",
                                                                  new ImagemapArea(0, 520, 520, 520)),
                                            new MessageImagemapAction("message1",
                                                                      new ImagemapArea(520, 0, 520, 520)),
                                            new MessageImagemapAction("message2",
                                                                      new ImagemapArea(520, 520, 520,
                                                                                       520))));
        messageSender.reply(replyToken, message);
    }

    private static String buildImageUrl(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }
}
