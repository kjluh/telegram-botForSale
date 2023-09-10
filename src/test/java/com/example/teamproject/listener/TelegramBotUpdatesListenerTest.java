package com.example.teamproject.listener;

import com.example.teamproject.entities.TypeOfDiscount;
import com.example.teamproject.entities.UserBot;
import com.example.teamproject.repositories.UserRepository;
import com.example.teamproject.service.CurrencyService;
import com.example.teamproject.service.WeatherService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TelegramBotUpdatesListenerTest {

    @MockBean
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givePriceTest() {
        String inputMessage = "1";
        String outMessage = "Выберите пункт из меню";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);

        InlineKeyboardButton[][] inlineKeyboardButtons = ((InlineKeyboardMarkup) testHandlerBySwitch(inputMessage)
                .getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(5);
    }

    @Test
    public void logisticTest() {
        String inputMessage = "логистика";
        String outMessage = "тут должна быть информация о доставке.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void paymentTest() {
        String inputMessage = "оплата";
        String outMessage = " тут должно быть описание способов оплаты";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void allPriceTest() {
        String inputMessage = "общий прайс";
        String outMessage = "text/plain";
        Assertions.assertThat(testHandlerBySwitch1(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch1(inputMessage).getContentType()).isEqualTo(outMessage);
    }


    @Test
    public void giveBlankTest() {
        String inputMessage = "2";
        String outMessage = "text/plain";
        Assertions.assertThat(testHandlerBySwitch1(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch1(inputMessage).getContentType()).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase2Test() throws Exception {
        when(currencyService.getEURExchangeRate()).thenReturn("test");
        when(currencyService.getUSDExchangeRate()).thenReturn("test1");
        String inputMessage = "3";
        String outMessage = "На " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                "\n$ - " + "test1" +
                "\n€ - " + "test";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void takeBlankTest() {
        String inputMessage = "4";
        String outMessage = "Введите номер телефона и Ваше имя в формате: 89001122333 Имя \nЗатем отправьте бланк заказа в чат";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void takeBlankTestExep() {
        UserBot userBot = new UserBot();
        userBot.setTypeDis(TypeOfDiscount.first);
        userBot.setChatId(123L);
        userRepository.save(userBot);
        String inputMessage = "4";
        String outMessage = "Отправьте бланк заказа в чат";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
        userRepository.delete(userBot);
    }

    @Test
    public void findWeatherTest() throws Exception {
        when(weatherService.getWeather(any())).thenReturn("testWeather");
        String inputMessage = "Пенза";
        String outMessage = "testWeather";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void helpManagerTest() {
        UserBot userBot = new UserBot();
        userBot.setChatId(123L);
        userRepository.save(userBot);
        String inputMessage = "позвать менеджера";
        String outMessage = "Ожидайте ответа от менеджера";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
        userRepository.delete(userBot);
    }

    @Test
    public void firstMenuTest() {
        String inputMessage = "Главное меню";
        String outMessage = "Добрый день!\nВыберите пункт из меню: ";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    private SendMessage testHandlerBySwitch(String inputMessage) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn(inputMessage);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);
        when(callbackQuery.data()).thenReturn(inputMessage);
        when(callbackQuery.message()).thenReturn(message);
        when(update.callbackQuery()).thenReturn(callbackQuery);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        when(telegramBot.execute(argumentCaptor.capture())).thenReturn(null);
        telegramBotUpdatesListener.process(List.of(update));
        SendMessage actual = argumentCaptor.getValue();
        return actual;
    }

    private SendDocument testHandlerBySwitch1(String inputMessage) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn(inputMessage);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);
        when(callbackQuery.data()).thenReturn(inputMessage);
        when(callbackQuery.message()).thenReturn(message);
        when(update.callbackQuery()).thenReturn(callbackQuery);

        ArgumentCaptor<SendDocument> argumentCaptor = ArgumentCaptor.forClass(SendDocument.class);
        when(telegramBot.execute(argumentCaptor.capture())).thenReturn(null);
        telegramBotUpdatesListener.process(List.of(update));
        SendDocument actual = argumentCaptor.getValue();
        return actual;
    }
}
