package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class RegisterRequestDTO {

  private String name;

  private String password;

  private Department department;

  private Level level;

  private String insertName;
}
