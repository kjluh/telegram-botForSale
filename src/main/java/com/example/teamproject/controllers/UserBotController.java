package com.example.teamproject.controllers;

import com.example.teamproject.entities.UserBot;
import com.example.teamproject.service.UserBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/opthim")
public class UserBotController {


    private final UserBotService userBotService;

    public UserBotController(UserBotService adoptiveParensService) {
        this.userBotService = adoptiveParensService;
    }

    @Operation(
            summary = "Добавление клиента в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавление усыновителя прошло успешно",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Ошибка: Неверный формат",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @PostMapping()
    public UserBot saveUserBot(@Parameter(description = "Данные клиента") @RequestBody UserBot newUserBot) {
        return userBotService.save(newUserBot);
    }

    @Operation(
            summary = "Посмотреть всех покупателей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Ошибка: покупателей не найдено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @GetMapping("/get_all")
    public ResponseEntity<Collection<UserBot>> getAllUserBot() {
        return ResponseEntity.ok(userBotService.findAll());
    }

    @Operation(
            summary = "Посмотреть покупателя по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Ошибка: покупатель не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @GetMapping("/get_by_id")
    public ResponseEntity<UserBot> getUserBotById(@Parameter(description = "id покупателя") @RequestParam Long id){
        return ResponseEntity.ok(userBotService.findUserBotById(id));
    }

    @Operation(
            summary = "Изменить покупателя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Ошибка: покупатель не найден или данные не корректны",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PutMapping
    public ResponseEntity<UserBot> updateUserBot(@Parameter(description = "Измененные данные покупателя") @RequestBody UserBot userBot){
        return ResponseEntity.ok(userBotService.updateUserBot(userBot));
    }

    @Operation(
            summary = "Удалить покупателя по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Ошибка: покупатель не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @DeleteMapping("{id}")
    public ResponseEntity deleteUserBotById(@Parameter(description = "id покупателя") @PathVariable Long id){
        userBotService.deleteUserBotById(id);
        return ResponseEntity.ok().build();
    }
}
