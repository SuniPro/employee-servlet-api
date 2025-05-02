package com.taekang.employeeservletapi.DTO.tether;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TetherAccountDTO {
    
    private Long id;

    private String tetherWallet;

    private String email;

    private LocalDateTime insertDateTime;

    private LocalDateTime updateDateTime;

    private LocalDateTime deleteDateTime;
}
