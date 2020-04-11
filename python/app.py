import os
import sys

from flask import Flask, request, abort
from linebot import LineBotApi, WebhookHandler
from linebot.exceptions import InvalidSignatureError
from linebot.models import (
    MessageEvent, PostbackEvent, UnfollowEvent, FollowEvent, JoinEvent, LeaveEvent,
    TextMessage, TextSendMessage, StickerMessage, StickerSendMessage,
    ConfirmTemplate, ButtonsTemplate, CarouselTemplate, CarouselColumn, TemplateSendMessage,
    QuickReply, QuickReplyButton, MessageAction, PostbackAction, URIAction, DatetimePickerAction,
    LocationAction, CameraAction, CameraRollAction)

app = Flask(__name__)

channel_secret = os.getenv("LINE_CHANNEL_SECRET", None)
channel_access_token = os.getenv("LINE_CHANNEL_ACCESS_TOKEN", None)
if channel_secret is None:
    app.logger.error("LINE_CHANNEL_SECRET not found.")
    sys.exit(1)
if channel_access_token is None:
    app.logger.error("LINE_CHANNEL_ACCESS_TOKEN not found.")
    sys.exit(1)

line_bot_api = LineBotApi(channel_access_token)
handler = WebhookHandler(channel_secret)


@app.route("/callback", methods=["POST"])
def callback():
    signature = request.headers["X-Line-Signature"]
    body = request.get_data(as_text=True)
    app.logger.info("Request body: " + body)
    try:
        handler.handle(body, signature)
    except InvalidSignatureError:
        abort(400)
    return "OK"


@handler.add(MessageEvent, message=TextMessage)
def handle_text_message(event):
    text = event.message.text
    if text == "confirm":
        # Confirm Template
        template = ConfirmTemplate(text="confirm text",
                                   actions=[MessageAction(label="Yes", text="Yes"),
                                            MessageAction(label="No", text="No")])
        line_bot_api.reply_message(event.reply_token,
                                   TemplateSendMessage(alt_text="confirm alt_text", template=template))
    elif text == "buttons":
        # Buttons Template
        template = ButtonsTemplate(title="buttons title",
                                   text="buttons text",
                                   actions=[
                                       URIAction(label="uri action", uri="https://line.me"),
                                       PostbackAction(label="postback action", data="data",
                                                      text="postback_text"),
                                       MessageAction(label="message action", text="message action")])
        line_bot_api.reply_message(event.reply_token,
                                   TemplateSendMessage(alt_text="buttons alt_text", template=template))
    elif text == "carousel":
        # Carousel Template
        template = CarouselTemplate(columns=[
            CarouselColumn(text="text1", title="title1",
                           actions=[
                               DatetimePickerAction(label="date picker", data="date_postback", mode="date"),
                               DatetimePickerAction(label="time picker", data="time_postback", mode="time"),
                               DatetimePickerAction(label="datetime picker", data="datetime_postback",
                                                    mode="datetime")
                           ]),
            CarouselColumn(text="text2", title="title2",
                           actions=[
                               URIAction(label="uri action", uri="https://line.me"),
                               PostbackAction(label="postback action", data="postback_data",
                                              text="postback_text"),
                               MessageAction(label="message action", text="message action")
                           ])
        ])
        line_bot_api.reply_message(event.reply_token,
                                   TemplateSendMessage(alt_text="buttons alt_text", template=template))
    elif text == "quick_reply":
        # Quick Reply
        quick_reply = QuickReply(items=[
            QuickReplyButton(action=PostbackAction(label="postback", data="data1")),
            QuickReplyButton(action=MessageAction(label="message", text="text2")),
            QuickReplyButton(action=DatetimePickerAction(label="datetime", data="data3", mode="date")),
            QuickReplyButton(action=CameraAction(label="camera")),
            QuickReplyButton(action=CameraRollAction(label="camera roll")),
            QuickReplyButton(action=LocationAction(label="location"))])
        line_bot_api.reply_message(event.reply_token,
                                   TextSendMessage(text="quick reply", quick_reply=quick_reply))
    else:
        line_bot_api.reply_message(event.reply_token,
                                   TextSendMessage(text=event.message.text))


@handler.add(MessageEvent, message=StickerMessage)
def handle_sticker_message(event):
    line_bot_api.reply_message(event.reply_token,
                               StickerSendMessage(package_id=event.message.package_id,
                                                  sticker_id=event.message.sticker_id))


@handler.add(PostbackEvent)
def handle_postback(event):
    line_bot_api.reply_message(event.reply_token,
                               TextSendMessage(text="postback: " + event.postback.data))


@handler.add(FollowEvent)
def handle_follow(event):
    line_bot_api.reply_message(event.reply_token,
                               TextSendMessage(text="Got follow event"))


@handler.add(UnfollowEvent)
def handle_unfollow():
    app.logger.info("Got Unfollow event")


@handler.add(JoinEvent)
def handle_join(event):
    line_bot_api.reply_message(event.reply_token,
                               TextSendMessage(text="Joined this " + event.source.type))


@handler.add(LeaveEvent)
def handle_leave():
    app.logger.info("Got leave event")


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080, debug=False)
