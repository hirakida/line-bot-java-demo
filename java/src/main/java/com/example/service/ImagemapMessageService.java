package com.example.service;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.client.MessagingClient;

import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapAction;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImagemapMessageService {
    private final MessagingClient messagingClient;

    public void replyMessage(String replyToken) {
        URI baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                 .path("imagemap")
                                                 .build()
                                                 .toUri();
        List<ImagemapAction> actions =
                List.of(URIImagemapAction.builder()
                                         .linkUri("https://line.me")
                                         .label("URIImagemapAction")
                                         .area(new ImagemapArea(0, 0, 520, 520))
                                         .build(),
                        URIImagemapAction.builder()
                                         .linkUri("https://developers.line.me/ja/")
                                         .label("URIImagemapAction")
                                         .area(new ImagemapArea(0, 520, 520, 520))
                                         .build(),
                        MessageImagemapAction.builder()
                                             .label("MessageImagemapAction")
                                             .text("message1")
                                             .area(new ImagemapArea(520, 0, 520, 520))
                                             .build(),
                        MessageImagemapAction.builder()
                                             .label("MessageImagemapAction")
                                             .text("message2")
                                             .area(new ImagemapArea(520, 520, 520, 520))
                                             .build());
        ImagemapMessage message = ImagemapMessage.builder()
                                                 .baseUrl(baseUrl)
                                                 .altText("Imagemap Template")
                                                 .baseSize(new ImagemapBaseSize(1040, 1040))
                                                 .actions(actions)
                                                 .build();

        messagingClient.reply(replyToken, message);
    }
}
