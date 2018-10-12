package com.example.service;

import static com.example.config.Constants.IMAGE_URL;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.linecorp.bot.model.action.CameraAction;
import com.linecorp.bot.model.action.CameraRollAction;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.LocationAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuickReplyService {
    private final MessageSender messageSender;

    public void replyQuickReply(String replyToken) {
        final List<QuickReplyItem> items =
                Arrays.asList(QuickReplyItem.builder()
                                            .action(CameraAction.withLabel("CameraAction"))
                                            .build(),
                              QuickReplyItem.builder()
                                            .action(CameraRollAction.withLabel("CemeraRollAction"))
                                            .build(),
                              QuickReplyItem.builder()
                                            .action(LocationAction.withLabel("LocationAction"))
                                            .build(),
                              QuickReplyItem.builder()
                                            .action(new MessageAction("MessageAction", "MessageAction"))
                                            .imageUrl(createURI(IMAGE_URL))
                                            .build(),
                              QuickReplyItem.builder()
                                            .action(PostbackAction.builder()
                                                                  .label("PostbackAction")
                                                                  .text("PostbackAction")
                                                                  .data("postback action")
                                                                  .build())
                                            .build(),
                              QuickReplyItem.builder()
                                            .action(new DatetimePickerAction("DatetimePickerAction",
                                                                             "datetime action",
                                                                             "datetime"))
                                            .build());
        final QuickReply quickReply = QuickReply.builder()
                                                .items(items)
                                                .build();
        final TextMessage message = TextMessage.builder()
                                               .text("Quick Reply")
                                               .quickReply(quickReply)
                                               .build();
        messageSender.reply(replyToken, message);
    }

    private static URI createURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
