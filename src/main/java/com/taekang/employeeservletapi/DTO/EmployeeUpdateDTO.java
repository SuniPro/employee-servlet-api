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
public class EmployeeUpdateDTO {

  private Long id;

  private Department department;

  private Level level;

  private String name;

  private String password;
}
