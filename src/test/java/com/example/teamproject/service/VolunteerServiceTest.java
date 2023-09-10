package com.example.teamproject.service;

import com.example.teamproject.entities.UserBot;
import com.example.teamproject.listener.TelegramBotUpdatesListener;
import com.example.teamproject.repositories.UserRepository;
import com.example.teamproject.service.VolunteerService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
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
public class VolunteerServiceTest {

    @Autowired
    private VolunteerService volunteerService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void volunteerServiceTest() {
        SendMessage sendMessage1 = new SendMessage(123L,"Введите номер телефона и вопрос в формате: 89001122333 Имя Ваш вопрос");
       Assertions.assertEquals(sendMessage1.getContentType(), volunteerService.sendMessageVolunteer(123L).getContentType());
    }

    @Test
    public void volunteerServiceTest1() {
        UserBot userBot = new UserBot();
        userBot.setChatId(123L);
        userBot.setName("alex");
        userBot.setPhoneNumber(89001122333L);
        when(userRepository.findUserBotByChatId(any())).thenReturn(userBot);
        SendMessage sendMessage1 = new SendMessage(123L,"Вас просит перезвонить покупатель: ");
        Assertions.assertEquals(sendMessage1.getContentType(), volunteerService.sendMessageVolunteer(123L).getContentType());
    }

}
