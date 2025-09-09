package com.taekang.employeeservletapi.DTO.crypto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taekang.employeeservletapi.entity.user.ChainType;
import com.taekang.employeeservletapi.entity.user.CryptoType;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CryptoDepositDTO {

  private Long id;

  private TransactionStatus status;

  private ChainType chainType;

  private CryptoType cryptoType;

  private String fromAddress;

  private String toAddress;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal krwAmount;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal realAmount;

  private Boolean accepted;

  private LocalDateTime acceptedAt;

  private LocalDateTime requestedAt;

  @JsonProperty("isSend")
  private boolean isSend;
}
