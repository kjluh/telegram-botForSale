package com.example.teamproject.service;

import com.example.teamproject.entities.UserBot;
import com.example.teamproject.repositories.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@EnableScheduling
public class ReminderForParents {

    private TelegramBot telegramBot;

    private VolunteerService volunteerService;

    private UserRepository userRepository;

    public ReminderForParents(TelegramBot telegramBot, VolunteerService volunteerService, UserRepository adoptiveParentRepository) {
        this.telegramBot = telegramBot;
        this.volunteerService = volunteerService;
        this.userRepository = adoptiveParentRepository;
    }

 // Как вариант реализовать для рассылки прайсов

//    @Scheduled(cron = "* 00 10 * * *")
//    public void checkTrialPeriod() {
//        List<UserBot> adoptiveParents = userRepository.findAll(); // получаем всех покупателей
//        for (UserBot aPs : adoptiveParents) {
//            Collection<Pet> pets = aPs.getPets();
//            for (Pet petNow : pets) {
//                if (petNow.getTrialPeriod().equals(LocalDate.now())) {
//                    telegramBot.execute(new SendMessage(aPs.getChatId(), "Поздравляем с "));
//                }
//            }
//        }
//    }
}