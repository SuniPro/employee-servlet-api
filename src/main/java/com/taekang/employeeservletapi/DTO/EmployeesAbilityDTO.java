package com.taekang.employeeservletapi.DTO;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesAbilityDTO {
  private Long employeeId;

  private AbilityDTO ability;

  private List<AbilityDTO> employeesAbilityList;
}
