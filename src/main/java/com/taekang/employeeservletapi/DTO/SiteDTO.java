package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.user.ChainType;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteDTO {
    
    private Long id;
    
    private String site;
    
    private String cryptoWallet;
    private ChainType chainType;

    private LocalDateTime insertDateTime;
    private String insertId;
    private LocalDateTime updateDateTime;
    private String updateId;
    private LocalDateTime deleteDateTime;
    private String deleteId;
}
