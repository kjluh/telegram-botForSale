package com.example.teamproject.service;

import com.example.teamproject.entities.TypeOfDiscount;
import com.example.teamproject.entities.UserBot;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TelegramBotService {

    private TelegramBot telegramBot;
    private UserBotService userBotService;


    @Value("${price.number.1}")
    private String price1;
    @Value("${price.number.2}")
    private String price2;
    @Value("${price.number.3}")
    private String price3;

    public TelegramBotService(TelegramBot telegramBot, UserBotService adoptiveParentService) {
        this.telegramBot = telegramBot;
        this.userBotService = adoptiveParentService;
    }

    /**
     * Метод приветствует пользователя.
     * Затем показывает пользователю начальное меню, использует {@link InlineKeyboardButton}
     *
     * @param chatId принимает ID чата, где отобразит кнопки
     */
    public void firstMenu(Long chatId) { // меню начальное, кейсы 1/2/3

        userBotService.saveUserBotDataBase(chatId);

        SendMessage helloMessage = new SendMessage(chatId, "Добрый день!\nВыберите пункт из меню: ");

        InlineKeyboardButton button1 = new InlineKeyboardButton("Получить оптовый прайс");
        button1.callbackData("1");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Получить бланк заказа");
        button2.callbackData("2");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Узнать курс валюты");
        button3.callbackData("3");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Отправить заказ в бланке заказа");
        button4.callbackData("4");
        InlineKeyboardButton button5 = new InlineKeyboardButton("Узнать погоду");
        button5.callbackData("5");
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.addRow(button1);
        keyboard.addRow(button2);
        keyboard.addRow(button3);
        keyboard.addRow(needManager());
        keyboard.addRow(button4);
        keyboard.addRow(button5);
        helloMessage.replyMarkup(keyboard);
        telegramBot.execute(helloMessage);
    }


    /**
     * Метод приветствует пользователя.
     * Затем показывает пользователю созданные кнопки 3-го этапа в чате с ботом, использует {@link InlineKeyboardButton}
     *
     * @param chatId принимает ID чата, где отобразит кнопки
     */
    public void givePrice(Long chatId) { // кнопки этапа 1, кейсы между 1 и 2

        SendMessage message = new SendMessage(chatId, "Выберите пункт из меню");

        InlineKeyboardButton button1 = new InlineKeyboardButton("Получить информацию о доставке");
        button1.callbackData("логистика");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Получить информацию об оплате");
        button2.callbackData("оплата");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Получить общий прайс");
        button3.callbackData("общий прайс");
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.addRow(button1);
        keyboard.addRow(button2);
        keyboard.addRow(button3);
        keyboard.addRow(needManager());
        keyboard.addRow(mainMenu());
        message.replyMarkup(keyboard);
        telegramBot.execute(message);
    }

    /**
     * Метод приветствует пользователя.
     * Затем показывает пользователю созданные кнопки 2-го этапа в чате с ботом, использует {@link InlineKeyboardButton}
     *
     * @param chatId принимает ID чата, где отобразит кнопки
     */
    public void giveBlank(Long chatId) {  // кнопки этапа 2, кейсы между 2 и 3
        SendDocument sendDocument = null;
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        UserBot userBot = userBotService.findUserBotByChatId(chatId);
        if (userBot == null || userBot.getTypeDis()==null) {
            sendDocument = new SendDocument(chatId,
                    new File(price1));
        } else {
            switch (userBot.getTypeDis()) {
                case first -> sendDocument = new SendDocument(chatId, new File(price1));
                case second -> sendDocument = new SendDocument(chatId, new File(price2));
            }
        }
        keyboard.addRow(needManager());
        keyboard.addRow(mainMenu());
        sendDocument.replyMarkup(keyboard);
        telegramBot.execute(sendDocument);
    }

    /**
     * Метод приветствует пользователя.
     * Затем показывает пользователю созданные кнопки 3-го этапа в чате с ботом, использует {@link InlineKeyboardButton}
     *
     * @param chatId принимает ID чата, где отобразит кнопки
     */
    public void findWeather(Long chatId) {  // кнопки этапа 3, кейсы между 2 и 3
        SendMessage message = new SendMessage(chatId, "Выберете город");
        InlineKeyboardButton button1 = new InlineKeyboardButton("Москва");
        button1.callbackData("Москва");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Воронеж");
        button2.callbackData("Воронеж");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Саратов");
        button3.callbackData("Саратов");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Самара");
        button4.callbackData("Самара");
        InlineKeyboardButton button5 = new InlineKeyboardButton("Рязань");
        button5.callbackData("Рязань");
        InlineKeyboardButton button6 = new InlineKeyboardButton("Пенза");
        button6.callbackData("Пенза");
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.addRow(button1);
        keyboard.addRow(button2);
        keyboard.addRow(button3);
        keyboard.addRow(button4);
        keyboard.addRow(button5);
        keyboard.addRow(button6);
        keyboard.addRow(needManager());
        keyboard.addRow(mainMenu());
        message.replyMarkup(keyboard);
        telegramBot.execute(message);
    }

    /**
     * Метод показывает пользователю кнопу "Позвать менеджера" в чате бота, использует {@link InlineKeyboardButton}
     *
     * @return возвращает созданную кнопку
     */
    public InlineKeyboardButton needManager() { // метод позвать волонтера
        InlineKeyboardButton button = new InlineKeyboardButton("Связаться с менеджером");
        button.callbackData("позвать менеджера");
        return button;
    }

    /**
     * Метод показывает пользователю кнопу "Главное меню" для возврата в основное меню в чате бота,
     * использует {@link InlineKeyboardButton}
     *
     * @return возвращает созданную кнопку
     */
    public InlineKeyboardButton mainMenu() { // // возврат в главное меню
        InlineKeyboardButton button = new InlineKeyboardButton("Главное меню");
        button.callbackData("Главное меню");
        return button;
    }
}
