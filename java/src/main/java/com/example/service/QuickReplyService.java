package com.example.service;

import static com.example.config.Constants.IMAGE_URL;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.client.MessagingClient;

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
    private final MessagingClient messagingClient;

    public void replyQuickReply(String replyToken) {
        List<QuickReplyItem> items =
                List.of(QuickReplyItem.builder()
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
                                      .imageUrl(URI.create(IMAGE_URL))
                                      .build(),
                        QuickReplyItem.builder()
                                      .action(PostbackAction.builder()
                                                            .label("PostbackAction")
                                                            .text("PostbackAction")
                                                            .data("postback action")
                                                            .build())
                                      .build(),
                        QuickReplyItem.builder()
                                      .action(DatetimePickerAction.OfLocalDatetime.builder()
                                                                                  .label("DatetimePickerAction")
                                                                                  .data("datetime action")
                                                                                  .initial(LocalDateTime.now())
                                                                                  .build())
                                      .build());
        QuickReply quickReply = QuickReply.builder()
                                          .items(items)
                                          .build();
        TextMessage message = TextMessage.builder()
                                         .text("Quick Reply")
                                         .quickReply(quickReply)
                                         .build();

        messagingClient.reply(replyToken, message);
    }
}
