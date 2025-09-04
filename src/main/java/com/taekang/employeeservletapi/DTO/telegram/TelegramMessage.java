package com.taekang.employeeservletapi.DTO.telegram;

import lombok.Data;

@Data
public class TelegramMessage {
    private TelegramChat chat;
    private String text;
}
