package com.taekang.employeeservletapi.DTO;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSiteDTO {
  private Long id;
  private String site;
}
