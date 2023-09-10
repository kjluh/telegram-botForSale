package com.example.teamproject.listener;

import com.example.teamproject.service.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {


    private TelegramBot telegramBot;
    private TelegramBotService telegramBotService;
    private UserBotService userBotService;
    private VolunteerService volunteerService;
    private CurrencyService currencyService;
    private WeatherService weatherService;
    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramBotService telegramBotService, UserBotService userBotService, VolunteerService volunteerService, CurrencyService currencyService, WeatherService weatherService) {
        this.telegramBot = telegramBot;
        this.telegramBotService = telegramBotService;
        this.userBotService = userBotService;
        this.volunteerService = volunteerService;
        this.currencyService = currencyService;
        this.weatherService = weatherService;
    }

    private Long chatId;
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Pattern для обработки сообщения от пользователя типа "89001122333 Имя вопрос"
     */
    private static final Pattern TELEPHONE_MESSAGE = Pattern.compile(
            "(\\d{11})(\\s)([А-яA-z)]+)(\\s)([А-яA-z)\\s\\d]+)"); // парсим сообщение на группы по круглым скобкам

    @Value("${price.number.1}")
    private String price1;

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);

                /**
                 * Отображение клавиатуры для пользователя с последующей обработкой кнопок
                 */
                if (update.callbackQuery() != null) {  // обработка этапа 0
                    chatId = update.callbackQuery().message().chat().id();
                    String data = update.callbackQuery().data();
                    switch (data) {
                        case "1" -> telegramBotService.givePrice(chatId);
                        case "логистика" ->
                                telegramBot.execute(new SendMessage(chatId, "тут должна быть информация о доставке."));
                        case "оплата" ->
                                telegramBot.execute(new SendMessage(chatId, " тут должно быть описание способов оплаты"));
                        case "общий прайс" ->
                                telegramBot.execute(new SendDocument(chatId, new File(price1)));
                        case "2" -> {
                            telegramBotService.giveBlank(chatId);
                        }
                        case "3" -> {
                            try {
                                telegramBot.execute(new SendMessage(chatId, "На " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                                        "\n$ - " + currencyService.getUSDExchangeRate()
                                        + "\n€ - " + currencyService.getEURExchangeRate()));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                        }
                        case "4" -> telegramBot.execute(telegramBotService.takeBlank(chatId));

                        case "5" -> telegramBotService.findWeather(chatId);

                        case "Москва", "Пенза", "Воронеж", "Саратов", "Самара", "Рязань" -> {
                            try {
                                telegramBot.execute(new SendMessage(chatId, weatherService.getWeather(data)));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        case "позвать менеджера" -> {
                            telegramBot.execute(volunteerService.sendMessageVolunteer(chatId));
                            telegramBot.execute(new SendMessage(chatId, "Ожидайте ответа от менеджера"));
                        }

                        case "Главное меню" -> telegramBotService.firstMenu(chatId);
                    }
                    return;
                }
                /**
                 * Создание и получение данных о пользователе из чата.
                 */
                chatId = update.message().chat().id();

                /**
                 * Проверяем сообщение пользователя на соответствие и сохраняем в БД,
                 * или выдаем информацию о несоответствии шаблону для сохранения.
                 */
                if (update.message().text() != null) {
                    Matcher matcher = TELEPHONE_MESSAGE.matcher(update.message().text());
                    if (matcher.find()) {  //find запускает matcher
                        telegramBot.execute(userBotService.saveInfoDataBase(matcher, chatId));
                    } else {
                        telegramBotService.firstMenu(chatId);
                    }
                }
                /**
                 * Проверка получения бланка заказа от пользователя
                 */
                if (update.message().document() != null) {
                    String fileId = update.message().document().fileId();
                    SendDocument sendDocument = new SendDocument(volunteerService.getVolunteerChat(), fileId);
                    telegramBot.execute(sendDocument);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
