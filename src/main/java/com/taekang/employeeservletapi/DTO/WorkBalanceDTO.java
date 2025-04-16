package com.taekang.employeeservletapi.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WorkBalanceDTO {

    private Long employeeId;

    private Integer workBalance;

    private LocalDateTime date;
}
