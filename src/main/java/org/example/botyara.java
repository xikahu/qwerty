package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class botyara extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "";

    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        var id = update.getMessage().getFrom().getId();   //здесь айди пользователя

        var text = update.getMessage().getText();  //здесь само сообщение
        var next = InlineKeyboardButton.builder()
                .text("Next").callbackData("next")
                .build();

        var back = InlineKeyboardButton.builder()
                .text("Back").callbackData("back")
                .build();

        var url = InlineKeyboardButton.builder()
                .text("Tutorial")
                .url("https://core.telegram.org/bots/api")
                .build();
        boolean screaming = false;
        InlineKeyboardMarkup keyboardM1;
        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next)).build();

//Buttons are wrapped in lists since each keyboard is a set of button rows
        InlineKeyboardMarkup keyboardM2;
        keyboardM2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();

        var txt = update.getMessage().getText();
        if(update.getMessage().isCommand()) {
            if (txt.equals("/scream"))
                screaming = true;
            else if (txt.equals("/whisper"))
                screaming = false;
            else if (txt.equals("/menu"))
                sendMenu(id, "<b>Menu 1</b>", keyboardM1);
            return;
        }
    }
        public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
            SendMessage sm = SendMessage.builder().chatId(who.toString())
                    .parseMode("HTML").text(txt)
                    .replyMarkup(kb).build();

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }


    private void buttonTap(Long id, String queryId, String data, int msgId, InlineKeyboardMarkup keyboardM2, InlineKeyboardMarkup keyboardM1) throws TelegramApiException {

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString()).messageId(msgId).build();

        if(data.equals("next")) {
            newTxt.setText("MENU 2");
            newKb.setReplyMarkup(keyboardM2);
        } else if(data.equals("back")) {
            newTxt.setText("MENU 1");
            newKb.setReplyMarkup(keyboardM1);
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
        execute(newKb);
    }


        // String a = "0";
        // if (text.indexOf("Создай пароль ")>=0){
        //     a = text.substring(text.length() - 4);
        //  }

        // if(text==a ){
        //   sendText(id, "kak ti ygadal?");
        // }



    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

}
