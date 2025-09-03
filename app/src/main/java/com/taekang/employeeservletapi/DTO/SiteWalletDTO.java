package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.user.ChainType;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SiteWalletDTO {

  private Long id;

  private String cryptoWallet;

  private ChainType chainType;
}
