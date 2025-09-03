package com.taekang.employeeservletapi.DTO;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemoDTO {
  private Long id;
  private String memo;
}
