package com.taekang.employeeservletapi.DTO.tether;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherDepositChangeStatusDTO {

  private Long depositId;

  private String tetherWallet;

  private BigDecimal amount;
}
