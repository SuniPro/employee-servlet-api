package com.taekang.employeeservletapi.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.*;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BalanceResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal balance;
}
