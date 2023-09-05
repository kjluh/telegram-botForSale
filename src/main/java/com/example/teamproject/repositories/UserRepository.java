package com.example.teamproject.repositories;


import com.example.teamproject.entities.UserBot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для хранения контактных данных пользователя, использует {@link JpaRepository#save(Object)}
 */
public interface UserRepository extends JpaRepository<UserBot, Long> {

     UserBot findUserBotByChatId(Long chatId);

}
