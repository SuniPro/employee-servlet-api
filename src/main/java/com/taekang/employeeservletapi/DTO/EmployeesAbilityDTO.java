package com.taekang.employeeservletapi.DTO;

import lombok.*;

import java.util.List;

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
