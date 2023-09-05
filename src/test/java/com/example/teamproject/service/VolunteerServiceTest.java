package com.example.teamproject.service;

import com.example.teamproject.listener.TelegramBotUpdatesListener;
import com.example.teamproject.service.VolunteerService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VolunteerServiceTest {

    @MockBean
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void volunteerServiceTest() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        User user = mock(User.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(user.username()).thenReturn("test");
        when(message.chat()).thenReturn(chat);
        when(callbackQuery.data()).thenReturn("позвать волонтера");
        when(callbackQuery.message()).thenReturn(message);
        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(update.callbackQuery().message().from()).thenReturn(user);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        when(telegramBot.execute(argumentCaptor.capture())).thenReturn(null);
        telegramBotUpdatesListener.process(List.of(update));
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo("@ТУТ ДОЛЖЕН БЫТЬ АДРЕС ВОЛОНТЕРА!!!");
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                "Вас просят присоединиться к чату " + "@" + update.callbackQuery().message().from().username());
    }
}
