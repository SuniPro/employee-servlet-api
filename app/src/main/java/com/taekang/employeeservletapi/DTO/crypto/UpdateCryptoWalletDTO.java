package com.taekang.employeeservletapi.DTO.crypto;

import com.taekang.employeeservletapi.entity.user.ChainType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCryptoWalletDTO {

  private Long id;

  private String cryptoWallet;

  private ChainType chainType;
}
