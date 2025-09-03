package com.taekang.employeeservletapi.DTO;

import java.time.LocalDateTime;
import lombok.*;

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
