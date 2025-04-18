package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.RegisterRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

  Employee createEmployee(RegisterRequestDTO registerRequestDTO);

  Employee updateEmployee(EmployeeDTO employeeDTO);

  Employee getEmployeeById(Long id);

  Employee getEmployeeByName(String name);

  Page<Employee> getAllEmployees(Pageable pageable);

  List<Employee> getEmployeeListByDepartment(Department department);

  List<Employee> getEmployeeListByLevel(Level level);

  void deleteEmployeeById(Long id);
}
