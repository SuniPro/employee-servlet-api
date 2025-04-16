package com.taekang.employeeservletapi.DTO.tether;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherAccountDTO {

    private Long id;

    private String tetherWallet;

    private String username;

    private LocalDateTime insertDateTime;

    private LocalDateTime updateDateTime;

    private LocalDateTime deleteDateTime;
}