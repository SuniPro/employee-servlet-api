package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Employee;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbilityDTO {

  private Long id;

  private Employee employee;

  private double attitude;

  private double creativity;

  private double workPerformance;

  private double teamwork;

  private double knowledgeLevel;
}
