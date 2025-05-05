package com.taekang.employeeservletapi.DTO.tether;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherAccountUpdateDTO {

  private Long id;

  private String site;

  private String memo;
}
