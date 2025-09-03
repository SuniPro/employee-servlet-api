package com.taekang.employeeservletapi.DTO;

import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class CreateSiteDTO {
    private String site;
    
    private List<SiteWalletDTO> siteWalletList;
}
