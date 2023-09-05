package com.example.teamproject.service;

import com.example.teamproject.entities.UserBot;
import com.example.teamproject.repositories.UserRepository;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.regex.Matcher;

@Service
public class UserBotService {
    private UserRepository repository;

    public UserBotService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод сохранения запроса пользователя в БД использует метод {@link JpaRepository#save(Object)}
     *
     * @param chatId      принимает id чата
     * @param name        принимает имя пользователя
     * @param messageText принимает сообщение пользователя
     * @param phoneNumber принимает номер телефона пользователя
     */
    private void addUserContact(Long chatId, String name, String messageText, String phoneNumber) {

        UserBot userBot = findUserBotByChatId(chatId);
        if (userBot == null) {
            userBot = new UserBot();
            userBot.setChatId(chatId);
        }
        userBot.setName(name);
        userBot.setMessage(messageText);
        userBot.setPhoneNumber(Long.parseLong(phoneNumber));
        repository.save(userBot);
    }

    /**
     * Получить пользователя со всеми питомцами по chatId
     *
     * @param chatId
     * @return UserBot
     */
    public UserBot findUserBotByChatId(Long chatId) {
        UserBot userBot = repository.findUserBotByChatId(chatId);
        return userBot;
    }

    /**
     * Получить пользователя по id
     *
     * @param id
     * @return UserBot
     */
    public UserBot findUserBotById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * @param matcher фильтр сообщения
     * @param chatId
     */
    public SendMessage saveInfoDataBase(Matcher matcher, Long chatId) {
        String phoneNumber = matcher.group(1); // получаем телефон
        String name = matcher.group(3); // получаем имя
        String messageText = matcher.group(5); // получаем текст сообщения
        addUserContact(chatId, name, messageText, phoneNumber); // создаем и пишем контакт в базу
        return new SendMessage(chatId, "Данные записаны, В ближайшее время мы с Вами свяжемся");
    }

    /**
     * Добавляем или обновляем в БД потенциального пользователя
     *
     * @param chatId ID чата пользователя
     */
    public void saveUserBotDataBase(Long chatId) {
        UserBot userBotByChatId = findUserBotByChatId(chatId);
        if (userBotByChatId == null) {
            userBotByChatId = new UserBot();
            userBotByChatId.setChatId(chatId);
            repository.save(userBotByChatId);
        }
        userBotByChatId.setChatId(chatId);
        repository.save(userBotByChatId);

    }

    /**
     * Найти всех пользователей
     *
     * @return all users
     */
    public Collection<UserBot> findAll() {
        return repository.findAll();
    }

    /**
     * Сохранить в БД нового пользователя
     *
     * @param newUserBot Сущность нового усыновителя
     * @return Возвращаем добавленную сущность в качестве положительного ответа
     */
    public UserBot save(UserBot newUserBot) {
        return repository.save(newUserBot);
    }

    /**
     * Обновление информации по пользователю
     *
     * @param userBot Обновленная информация по усыновителю
     * @return Возвращаем обновленную сущность в качестве положительного ответа
     */
    public UserBot updateUserBot(UserBot userBot) {
        return repository.save(userBot);
    }

    /**
     * Удаление пользователя из БД
     *
     * @param id id пользователя
     */
    public void deleteUserBotById(Long id) {
        repository.deleteById(id);
    }
}
