package com.taekang.employeeservletapi.DTO.crypto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taekang.employeeservletapi.entity.user.CryptoType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DepositSentApprovalNotifyDTO {

  private String email;

  private CryptoType cryptoType;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal realAmount;

  private LocalDateTime requestAt;
}
