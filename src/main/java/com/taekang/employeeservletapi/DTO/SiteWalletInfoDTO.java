package com.taekang.employeeservletapi.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taekang.employeeservletapi.entity.user.ChainType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SiteWalletInfoDTO {
    
    private Long id;
    
    private String cryptoWallet;

    private ChainType chainType;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal balance;

    private long depositHistoryLength;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal todayDepositAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal weeksDepositAmount;

    private LocalDateTime insertDateTime;

    private LocalDateTime updateDateTime;
}
