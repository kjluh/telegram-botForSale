package com.example.teamproject.listener;

import com.example.teamproject.entities.UserBot;
import com.example.teamproject.entities.TypeOfDiscount;
import com.example.teamproject.service.UserBotService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TelegramBotUpdatesListenerTest {

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private UserBot adoptiveParent;

    @MockBean
    private UserBotService adoptiveParentService;

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void handlerStartMessageTest() {
        String inputMessage = "/start";
        String outMessage = "Выберите какой приют Вам нужен";
        Assertions.assertThat(testHandlerByChatMassage(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerByChatMassage(inputMessage).getParameters().get("text")).isEqualTo(outMessage);

        InlineKeyboardButton[][] inlineKeyboardButtons = ((InlineKeyboardMarkup) testHandlerByChatMassage(inputMessage)
                .getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons[0 ].length).isEqualTo(2); //1 ряд кнопок, но в ряду 2 кнопки
    }

    @Test
    public void handlerStartMessageTestFirstMenu() {
        String inputMessage = "Главное меню";
        String outMessage = "Выберите какой приют Вам нужен";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceShelterInfoTest() {
        String inputMessage = "1";
        String outMessage = "Приветствует в нашем приюте";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);

        InlineKeyboardButton[][] inlineKeyboardButtons = ((InlineKeyboardMarkup) testHandlerBySwitch(inputMessage)
                .getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(6);
    }

    @Test
    public void telegramBotServiceShelterInfoCase1Test() {
        String inputMessage = "инфа о приюте";
        String outMessage = "тут должна быть информация о приюте.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceShelterInfoCase2Test() {
        String inputMessage = "расписание работы";
        String outMessage = " тут должно быть расписание работы приюта и адрес, схему проезда.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceShelterInfoCase3Test() {
        String inputMessage = "рекомендации о ТБ";
        String outMessage = "тут должны быть общие рекомендации о технике безопасности на территории приюта.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }


    @Test
    public void telegramBotServiceTakeDogFromShelterCase1Test() {
        String inputMessage = "Правила знакомства";
        String outMessage = " тут должны быть правила знакомства с собакой до того, как можно забрать ее из приюта.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase2Test() {
        String inputMessage = "Список документов";
        String outMessage = " тут должен быть список документов, необходимых для того, чтобы взять собаку из приюта.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase3Test() {
        String inputMessage = "транспортировка животного";
        String outMessage = " тут должно быть список рекомендаций по транспортировке животного.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase4Test() {
        String inputMessage = "дома для щенка";
        String outMessage = " тут должно быть список рекомендаций по обустройству дома для щенка.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase5Test() {
        String inputMessage = "дома для собаки";
        String outMessage = " тут должно быть список рекомендаций по обустройству дома для взрослой собаки.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase6Test() {
        String inputMessage = "дома для собаки с ограничениями";
        String outMessage = " тут должен быть список рекомендаций по обустройству дома для собаки с ограниченными возможностями (зрение, передвижение).";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase7Test() {
        String inputMessage = "советы кинолога";
        String outMessage = " тут должны быть советы кинолога по первичному общению с собакой";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase8Test() {
        String inputMessage = "список кинологов";
        String outMessage = " тут должны быть рекомендации по проверенным кинологам для дальнейшего обращения к ним.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceTakeDogFromShelterCase9Test() {
        String inputMessage = "список причин для отказа";
        String outMessage = " тут должен быть список причин, почему могут отказать и не дать забрать собаку из приюта.";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceSendReportTest() {
        String inputMessage = "3";
        String outMessage = "Приветствует в нашем приюте";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);

        InlineKeyboardButton[][] inlineKeyboardButtons = ((InlineKeyboardMarkup) testHandlerBySwitch(inputMessage)
                .getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(4);
    }

    @Test
    public void telegramBotServiceSendReport1Test() {
        String inputMessage = "Форма ежедневного отчёта";
        String outMessage = "Форма ежедневного отчёта:\n1. Фото животного\n2. Рацион животного\n" +
                "3. Общее самочувствие и привыкание к новому месту \n4. Изменение в поведении";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceSendReport2Test() {
        String inputMessage = "принимаем отчет";
        String outMessage = "Напишите Id животного, для которого составляется отчёт";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    @Test
    public void telegramBotServiceSendReport3Test() {
        String inputMessage = "записать данные";
        String outMessage = "Введите номер телефона и вопрос в формате: 89001122333 Имя Ваш вопрос";
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(testHandlerBySwitch(inputMessage).getParameters().get("text")).isEqualTo(outMessage);
    }

    private SendMessage testHandlerByChatMassage(String testMessage) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn(testMessage);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        when(telegramBot.execute(argumentCaptor.capture())).thenReturn(null);
        telegramBotUpdatesListener.process(List.of(update));
        SendMessage actual = argumentCaptor.getValue();
        return actual;
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
}
