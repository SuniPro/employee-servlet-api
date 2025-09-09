package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

  private Long id;

  private Department department;

  private String site;

  private Level level;

  private String name;

  private String insertName;

  private LocalDateTime insertDateTime;

  private String updateName;

  private LocalDateTime updateDateTime;

  private String deleteName;

  private LocalDateTime deleteDateTime;
}
