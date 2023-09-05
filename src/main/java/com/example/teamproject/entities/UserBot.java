package com.example.teamproject.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Сущность для создания сообщения от пользователя и последующим хранением в бд
 */
@Data
@Entity
public class UserBot {
    /**
     * Уникальный ID для хранения класса в БД и использования экземпляра в программе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID чата от куда пришел пользователь
     */
    private Long chatId;

    /**
     * Имя пользователя
     */
    private String name;

    /**
     * Номер телефона пользователя
     */
    private Long phoneNumber;

    /**
     * Адрес пользователя
     */
    private String address;

    /**
     * Сообщение от пользователя
     */
    private String message;

    /**
     * Сохраняем статус скидки
     */
    @Enumerated(EnumType.STRING)
    private TypeOfDiscount typeDis;
}
