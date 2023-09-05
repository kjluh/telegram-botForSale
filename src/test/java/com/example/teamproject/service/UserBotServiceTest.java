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
    UserBotService adoptiveParentService;
    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    private UserBot adoptiveParent = new UserBot();
    @MockBean
    TelegramBot telegramBot;
    @MockBean
    UserRepository adoptiveParentRepository;
    @MockBean
    TelegramBotService telegramBotService;
    @MockBean
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        Long chatId = 123L;
        String name = "Ben";
        String messageText = "qwerty";
        String phoneNumber = "89000000000";
        adoptiveParent.setTypeDis(TypeOfDiscount.first);
        adoptiveParent.setChatId(chatId);
        adoptiveParent.setName(name);
        adoptiveParent.setMessage(messageText);
        adoptiveParent.setPhoneNumber(Long.parseLong(phoneNumber));
    }

    @Test
    public void addUserContactTest() {
        when(adoptiveParentRepository.save(any())).thenReturn(adoptiveParent);
        assertEquals(adoptiveParentService.save(adoptiveParent), adoptiveParent);
    }

    @Test
    public void findAdoptiveParentByChatIdTest() {
        when(adoptiveParentRepository.findUserBotByChatId(any())).thenReturn(adoptiveParent);
        assertEquals(adoptiveParentRepository.findUserBotByChatId(123L), adoptiveParent);
    }

    @Test
    public void findAdoptiveParentByIdTest() {
        when(adoptiveParentRepository.findById(any())).thenReturn(Optional.of(adoptiveParent));
        assertEquals(adoptiveParentRepository.findById(1L), Optional.of(adoptiveParent));
    }

    @Test
    public void saveInfoDataBaseTest() {
// НЕ ИДЕТ
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(123L);
        when(adoptiveParentService.findUserBotByChatId(any())).thenReturn(adoptiveParent);
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
        when(adoptiveParentRepository.save(any())).thenReturn(adoptiveParent);
        assertEquals(adoptiveParentService.save(adoptiveParent), adoptiveParent);
    }

    @Test
    public void findAllTest() {
        ArrayList<UserBot> adoptiveParents = new ArrayList<>();
        adoptiveParents.add(adoptiveParent);
        adoptiveParents.add(adoptiveParent);
        adoptiveParents.add(adoptiveParent);

        when(adoptiveParentRepository.findAll()).thenReturn(adoptiveParents);
    }

    @Test
    public void saveTest() {
        when(adoptiveParentRepository.save(any())).thenReturn(adoptiveParent);
        assertEquals(adoptiveParentService.save(adoptiveParent), adoptiveParent);
    }

    @Test
    public void updateAdoptiveParentTest() {
        when(adoptiveParentRepository.save(any())).thenReturn(adoptiveParent);
        assertEquals(adoptiveParentService.save(adoptiveParent), adoptiveParent);
    }

}
