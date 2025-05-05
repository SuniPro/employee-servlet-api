package com.taekang.employeeservletapi.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AbilityReviewDTO {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private double creativity;

    private double workPerformance;

    private double teamwork;

    private double knowledgeLevel;

    private LocalDate reviewDate;
}
