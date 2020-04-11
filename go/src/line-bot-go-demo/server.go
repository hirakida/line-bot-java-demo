package main

import (
	"log"
	"net/http"
	"os"

	"github.com/line/line-bot-sdk-go/linebot"
	"github.com/line/line-bot-sdk-go/linebot/httphandler"
)

func main() {
	handler, err := httphandler.New(
		os.Getenv("CHANNEL_SECRET"),
		os.Getenv("CHANNEL_ACCESS_TOKEN"),
	)
	if err != nil {
		log.Fatal(err)
	}

	handler.HandleEvents(func(events []*linebot.Event, r *http.Request) {
		bot, err := handler.NewClient()
		if err != nil {
			log.Print(err)
			return
		}
		for _, event := range events {
			if event.Type == linebot.EventTypeMessage {
				switch message := event.Message.(type) {
				case *linebot.TextMessage:
					if err := handleText(bot, message, event.ReplyToken); err != nil {
						log.Print(err)
					}
				case *linebot.StickerMessage:
					if err := handleSticker(bot, message, event.ReplyToken); err != nil {
						log.Print(err)
					}
				default:
					log.Printf("Unknown message: %v", message)
				}
			}
		}
	})
	http.Handle("/callback", handler)

	if err := http.ListenAndServe(":"+os.Getenv("PORT"), nil); err != nil {
		log.Fatal(err)
	}
}

func handleText(bot *linebot.Client, message *linebot.TextMessage, replyToken string) error {
	if _, err := bot.ReplyMessage(
		replyToken,
		linebot.NewTextMessage(message.Text),
	).Do(); err != nil {
		return err
	}
	return nil
}

func handleSticker(bot *linebot.Client, message *linebot.StickerMessage, replyToken string) error {
	if _, err := bot.ReplyMessage(
		replyToken,
		linebot.NewStickerMessage(message.PackageID, message.StickerID),
	).Do(); err != nil {
		return err
	}
	return nil
}
