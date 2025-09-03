package com.taekang.employeeservletapi.DTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteDTO {

  private Long id;

  private String site;

  private List<SiteWalletDTO> siteWalletList;

  private String telegramUsername;
  private Long telegramChatId;

  private LocalDateTime insertDateTime;
  private String insertId;
  private LocalDateTime updateDateTime;
  private String updateId;
  private LocalDateTime deleteDateTime;
  private String deleteId;
}
