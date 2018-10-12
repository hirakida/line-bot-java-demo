package com.example.service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuArea;
import com.linecorp.bot.model.richmenu.RichMenuBounds;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;
import com.linecorp.bot.model.richmenu.RichMenuSize;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RichMenuService {
    private final LineMessagingClient lineMessagingClient;

    public String create(String name, String chatBarText) {
        final RichMenuArea richMenuArea = new RichMenuArea(new RichMenuBounds(0, 0, 2500, 1686),
                                                           new MessageAction("MessageAction", "hello"));
        final RichMenu richMenu = RichMenu.builder()
                                          .size(RichMenuSize.FULL)
                                          .selected(true)
                                          .name(name)
                                          .chatBarText(chatBarText)
                                          .areas(Collections.singletonList(richMenuArea))
                                          .build();
        try {
            return lineMessagingClient.createRichMenu(richMenu).get()
                                      .getRichMenuId();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RichMenuResponse> getList() {
        try {
            return lineMessagingClient.getRichMenuList().get()
                                      .getRichMenus();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
