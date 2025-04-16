package com.taekang.employeeservletapi.DTO;

import java.time.LocalDateTime;

public record NotifyWithReadDTO(
        Long id,
        String title,
        String contents,
        boolean read,
        LocalDateTime readTime
) {}
