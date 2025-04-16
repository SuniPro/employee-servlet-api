package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeDTO {

    private Long id;

    private Department department;

    private Level level;

    private String name;

    private String insertName;

    private LocalDateTime insertDateTime;

    private String updateName;

    private LocalDateTime updateDateTime;

    private String deleteName;

    private LocalDateTime deleteDateTime;
}