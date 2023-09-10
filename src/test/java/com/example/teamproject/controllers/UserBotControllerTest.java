package com.example.teamproject.controllers;

import com.example.teamproject.entities.UserBot;
import com.example.teamproject.service.CurrencyService;
import com.example.teamproject.service.TelegramBotService;
import com.example.teamproject.service.UserBotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserBotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserBot userBot = new UserBot();
    private JSONObject jsonObject = new JSONObject();
    private Long chatId;
    private String name;
    private String messageText;
    private String phoneNumber;


    @MockBean
    private UserBotService userBotServiceMock;

    @BeforeEach
    void setUp() throws JSONException {
        chatId = 123L;
        name = "Ben";
        messageText = "qwerty";
        phoneNumber = "89000000000";

        userBot.setChatId(chatId);
        userBot.setName(name);
        userBot.setMessage(messageText);
        userBot.setPhoneNumber(Long.parseLong(phoneNumber));

        jsonObject.put("chatId", chatId);
        jsonObject.put("name", name);
        jsonObject.put("message", messageText);
        jsonObject.put("phoneNumber", phoneNumber);
    }

    @Test
    public void saveUserBotTest() throws Exception {
        when(userBotServiceMock.save(any(UserBot.class))).thenReturn(userBot);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/opthim")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.message").value(messageText))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber));
    }

    @Test
    public void getAllUserBotTest() throws Exception {
        ArrayList<UserBot> userBots = new ArrayList<>();
        userBots.add(userBot);

        when(userBotServiceMock.findAll()).thenReturn(userBots);

        mockMvc.perform(
                get("/opthim/get_all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userBots)));

    }

    @Test
    public void getAUserBotByIdTest() throws Exception {
        when(userBotServiceMock.findUserBotById(any(Long.class))).thenReturn(userBot);

        mockMvc.perform(
                        get("/opthim/get_by_id?id=123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.message").value(messageText))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber));

    }

    @Test
    public void updateUserBotTest() throws Exception {
        when(userBotServiceMock.updateUserBot(any(UserBot.class))).thenReturn(userBot);

        mockMvc.perform(
                        put("/opthim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.message").value(messageText))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber));
    }

    @Test
    public void deleteUserBotByIdTest() throws Exception {
        mockMvc.perform(
                        delete("/opthim/123"))
                .andExpect(status().isOk());

    }
}
