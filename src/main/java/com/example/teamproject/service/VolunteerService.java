package com.example.teamproject.service;

import com.example.teamproject.entities.UserBot;
import com.example.teamproject.repositories.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для приглашения менеджера в чат с клиентом
 */
@Service
@Data
public class VolunteerService {

    private UserRepository repository;

    public VolunteerService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Сюда нужно будет вписать ник в телеграме администратора
     */
    private int volunteerChat = 1653160516; // тут необходимо внести ид чата менеджера с ботом для получения сообщений

    /**
     * Метод пригласить в текущий чат менеджера
     */
    public SendMessage sendMessageVolunteer(Long chatId) {
        if (findUserBot(chatId) != null) {
            return new SendMessage(volunteerChat, "Вас просит перезвонить покупатель: "
                    + repository.findUserBotByChatId(chatId).getName() + " "
                    + repository.findUserBotByChatId(chatId).getPhoneNumber()
            );
        }
        return new SendMessage(chatId, "Введите номер телефона и вопрос в формате: 89001122333 Имя Ваш вопрос");
    }

    private String findUserBot(Long chatId) {
        UserBot userBot = repository.findUserBotByChatId(chatId);
        if (userBot != null || userBot.getPhoneNumber() != null) {
            return userBot.getPhoneNumber().toString();
        }
        return null;
    }
}
