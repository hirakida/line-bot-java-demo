'use strict';

const line = require('@line/bot-sdk');
const express = require('express');

const config = {
  channelAccessToken: process.env.CHANNEL_ACCESS_TOKEN,
  channelSecret: process.env.CHANNEL_SECRET,
};

const client = new line.Client(config);
const app = express();

app.post('/callback', line.middleware(config), (req, res) => {
  if (!Array.isArray(req.body.events)) {
    return res.status(500).end();
  }

  Promise.all(req.body.events.map(handleEvent))
         .then(() => res.end())
         .catch((err) => {
           console.error(err);
           res.status(500).end();
         });
});

const replyText = (token, texts) => {
  texts = Array.isArray(texts) ? texts : [texts];
  return client.replyMessage(token, texts.map((text) => ({type: 'text', text})));
};

function handleEvent(event) {
  switch (event.type) {
    case 'message':
      const message = event.message;
      switch (message.type) {
        case 'text':
          return handleText(message, event.replyToken, event.source);
        case 'sticker':
          return handleSticker(message, event.replyToken);
        case 'image':
        case 'video':
        case 'audio':
        case 'location':
          return console.log(`message event: ${message.type}`);
        default:
          throw new Error(`Unknown message: ${JSON.stringify(message)}`);
      }
    case 'follow':
      return replyText(event.replyToken, 'Got followed event');
    case 'unfollow':
      return console.log(`Unfollowed this bot: ${JSON.stringify(event)}`);
    case 'join':
      return replyText(event.replyToken, `Joined ${event.source.type}`);
    case 'leave':
      return console.log(`Left: ${JSON.stringify(event)}`);
    case 'postback':
      let data = event.postback.data;
      if (data === 'DATE' || data === 'TIME' || data === 'DATETIME') {
        data += `(${JSON.stringify(event.postback.params)})`;
      }
      return replyText(event.replyToken, `Got postback: ${data}`);
    case 'beacon':
      return replyText(event.replyToken, `Got beacon: ${event.beacon.hwid}`);
    default:
      throw new Error(`Unknown event: ${JSON.stringify(event)}`);
  }
}

function handleText(message, replyToken, source) {
  return replyText(replyToken, message.text);
}

function handleSticker(message, replyToken) {
  return client.replyMessage(
    replyToken,
    {
      type: 'sticker',
      packageId: message.packageId,
      stickerId: message.stickerId,
    }
  );
}

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`Server running on ${port}`);
});
