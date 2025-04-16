package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Employee;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommuteDTO {

  private Employee employee;

  private LocalDateTime onTime;

  private LocalDateTime offTime;
}
