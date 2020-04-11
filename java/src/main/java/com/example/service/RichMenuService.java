package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.client.MessagingClient;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuArea;
import com.linecorp.bot.model.richmenu.RichMenuBounds;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuSize;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RichMenuService {
    private final MessagingClient messagingClient;

    public RichMenuIdResponse create(String name, String chatBarText) {
        RichMenuArea richMenuArea = new RichMenuArea(new RichMenuBounds(0, 0, 2500, 1686),
                                                     new MessageAction("MessageAction", "hello"));
        RichMenu richMenu = RichMenu.builder()
                                    .size(RichMenuSize.FULL)
                                    .selected(true)
                                    .name(name)
                                    .chatBarText(chatBarText)
                                    .areas(List.of(richMenuArea))
                                    .build();

        return messagingClient.createRichMenu(richMenu);
    }
}
