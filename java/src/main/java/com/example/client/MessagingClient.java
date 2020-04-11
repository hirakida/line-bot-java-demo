package com.example.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;
import com.linecorp.bot.model.response.demographics.GetFriendsDemographicsResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessagingClient {
    private final LineMessagingClient lineMessagingClient;

    public BotApiResponse replyText(String replyToken, String message) {
        return reply(replyToken, new TextMessage(message));
    }

    public BotApiResponse reply(String replyToken, Message message) {
        return reply(replyToken, List.of(message));
    }

    public BotApiResponse reply(String replyToken, List<Message> messages) {
        ReplyMessage message = new ReplyMessage(replyToken, messages);
        return lineMessagingClient.replyMessage(message)
                                  .join();
    }

    public BotApiResponse push(String to, Message message) {
        return push(to, List.of(message));
    }

    public BotApiResponse push(String to, List<Message> messages) {
        PushMessage pushMessage = new PushMessage(to, messages);
        return lineMessagingClient.pushMessage(pushMessage)
                                  .join();
    }

    public BotApiResponse narrowcast(Message message, Filter filter) {
        Narrowcast narrowcast = new Narrowcast(message, filter);
        return lineMessagingClient.narrowcast(narrowcast)
                                  .join();
    }

    public NarrowcastProgressResponse getNarrowcastProgress(String requestId) {
        return lineMessagingClient.getNarrowcastProgress(requestId)
                                  .join();
    }

    public BotApiResponse broadcast(Message message) {
        Broadcast broadcast = new Broadcast(message);
        return lineMessagingClient.broadcast(broadcast)
                                  .join();
    }

    public MessageQuotaResponse getMessageQuota() {
        return lineMessagingClient.getMessageQuota()
                                  .join();
    }

    public QuotaConsumptionResponse getMessageQuotaConsumption() {
        return lineMessagingClient.getMessageQuotaConsumption()
                                  .join();
    }

    public NumberOfMessagesResponse getNumberOfSentReplyMessages(String date) {
        return lineMessagingClient.getNumberOfSentReplyMessages(date)
                                  .join();
    }

    public NumberOfMessagesResponse getNumberOfSentPushMessages(String date) {
        return lineMessagingClient.getNumberOfSentPushMessages(date)
                                  .join();
    }

    public NumberOfMessagesResponse getNumberOfSentMulticastMessages(String date) {
        return lineMessagingClient.getNumberOfSentMulticastMessages(date)
                                  .join();
    }

    public NumberOfMessagesResponse getNumberOfSentBroadcastMessages(String date) {
        return lineMessagingClient.getNumberOfSentBroadcastMessages(date)
                                  .join();
    }

    public GetNumberOfMessageDeliveriesResponse getNumberOfMessageDeliveries(String date) {
        return lineMessagingClient.getNumberOfMessageDeliveries(date)
                                  .join();
    }

    public GetNumberOfFollowersResponse getNumberOfFollowers(String date) {
        return lineMessagingClient.getNumberOfFollowers(date)
                                  .join();
    }

    public GetFriendsDemographicsResponse getFriendsDemographics() {
        return lineMessagingClient.getFriendsDemographics()
                                  .join();
    }

    public UserProfileResponse getProfile(String userId) {
        return lineMessagingClient.getProfile(userId)
                                  .join();
    }

    public RichMenuIdResponse createRichMenu(RichMenu richMenu) {
        return lineMessagingClient.createRichMenu(richMenu)
                                  .join();
    }

    public RichMenuListResponse getRichMenuList() {
        return lineMessagingClient.getRichMenuList()
                                  .join();
    }
}
