package com.taekang.employeeservletapi.DTO.tether;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherDepositSummaryDTO {

  private BigDecimal totalAmount;

  private long depositLength;

  private String maximumDepositor;

  private BigDecimal maximumAmount;
}
