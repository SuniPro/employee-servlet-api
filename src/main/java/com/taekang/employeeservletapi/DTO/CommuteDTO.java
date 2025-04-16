package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommuteDTO {

    private Employee employee;

    private LocalDateTime onTime;

    private LocalDateTime offTime;
}
