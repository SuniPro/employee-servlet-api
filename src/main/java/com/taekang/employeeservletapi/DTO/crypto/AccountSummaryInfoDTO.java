package com.taekang.employeeservletapi.DTO.crypto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryInfoDTO {

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal balance;

  private long depositHistoryLength;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal todayDepositAmount;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal weeksDepositAmount;
}
