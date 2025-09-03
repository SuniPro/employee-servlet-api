package com.taekang.employeeservletapi.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteOnlyDTO {

    private Long id;

    private String site;

    private String telegramUsername;
    private Long telegramChatId;

    private LocalDateTime insertDateTime;
    private String insertId;
    private LocalDateTime updateDateTime;
    private String updateId;
    private LocalDateTime deleteDateTime;
    private String deleteId;
}
