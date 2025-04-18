package com.taekang.employeeservletapi.DTO.tether;

import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TetherDepositDTO {

    private Long id;

    private String tetherWallet;

    private String username;

    private LocalDateTime insertDateTime;

    private BigDecimal amount;

    private Boolean accepted;

    private LocalDateTime acceptedAt;

    private LocalDateTime requestedAt;

    private TransactionStatus status;
}
