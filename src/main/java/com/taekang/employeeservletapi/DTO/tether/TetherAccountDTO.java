package com.taekang.employeeservletapi.DTO.tether;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TetherAccountDTO {

  private Long id;

  private String tetherWallet;

  private String email;

  private String site;

  private String memo;

  private LocalDateTime insertDateTime;

  private LocalDateTime updateDateTime;

  private LocalDateTime deleteDateTime;
}
