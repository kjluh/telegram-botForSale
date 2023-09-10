package com.example.teamproject.service;

import com.example.teamproject.entities.TypeOfDiscount;
import com.example.teamproject.entities.UserBot;
import com.example.teamproject.listener.TelegramBotUpdatesListener;
import com.example.teamproject.repositories.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class UserBotServiceTest {

    @Autowired
    UserBotService userBotService;
    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    private UserBot userBot = new UserBot();
    @MockBean
    TelegramBot telegramBot;
    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Long chatId = 123L;
        String name = "Ben";
        String messageText = "qwerty";
        String phoneNumber = "89000000000";
        userBot.setTypeDis(TypeOfDiscount.first);
        userBot.setChatId(chatId);
        userBot.setName(name);
        userBot.setMessage(messageText);
        userBot.setPhoneNumber(Long.parseLong(phoneNumber));
    }

    @Test
    public void addUserContactTest() {
        when(userRepository.save(any())).thenReturn(userBot);
        assertEquals(userBotService.save(userBot), userBot);
    }

    @Test
    public void findAdoptiveParentByChatIdTest() {
        when(userRepository.findUserBotByChatId(any())).thenReturn(userBot);
        assertEquals(userRepository.findUserBotByChatId(123L), userBot);
    }

    @Test
    public void findAdoptiveParentByIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(userBot));
        assertEquals(userRepository.findById(1L), Optional.of(userBot));
    }

    @Test
    public void saveInfoDataBaseTest() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(123L);
        when(userBotService.findUserBotByChatId(any())).thenReturn(userBot);
        when(message.text()).thenReturn("89991122333 name text message");
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        when(telegramBot.execute(argumentCaptor.capture())).thenReturn(null);
        telegramBotUpdatesListener.process(List.of(update));
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Данные записаны, В ближайшее время мы с Вами свяжемся");
    }

    @Test
    public void saveParentDataBaseTest() {
        when(userRepository.save(any())).thenReturn(userBot);
        assertEquals(userBotService.save(userBot), userBot);
    }

    @Test
    public void findAllTest() {
        ArrayList<UserBot> adoptiveParents = new ArrayList<>();
        adoptiveParents.add(userBot);
        adoptiveParents.add(userBot);
        adoptiveParents.add(userBot);

        when(userRepository.findAll()).thenReturn(adoptiveParents);
    }

    @Test
    public void saveTest() {
        when(userRepository.save(any())).thenReturn(userBot);
        assertEquals(userBotService.save(userBot), userBot);
    }

    @Test
    public void updateAdoptiveParentTest() {
        when(userRepository.save(any())).thenReturn(userBot);
        assertEquals(userBotService.save(userBot), userBot);
    }

}
