package com.taekang.employeeservletapi.DTO.tether;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherDepositRequestDTO {

    private String tetherWallet;

    private BigDecimal amount;
}
