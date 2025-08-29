package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.user.ChainType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class CreateSiteDTO {
    private String site;
    private String cryptoWallet;
    private ChainType chainType;
}
