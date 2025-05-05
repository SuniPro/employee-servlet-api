package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.RegisterRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

  Employee createEmployee(RegisterRequestDTO registerRequestDTO);

  Employee updateEmployee(EmployeeDTO employeeDTO);

  Employee getEmployeeById(Long id);

  Employee getEmployeeByName(String name);

  Page<Employee> getAllEmployees(Pageable pageable);

  List<Employee> getEmployeeListByDepartment(Department department);

  List<Employee> getEmployeeListByLevel(Level level);

  Page<EmployeeDTO> getEmployeeListByLevelLessThen(Level level, Pageable pageable);

  Page<EmployeeDTO> getEmployeeListByLevelAndDepartmentLessThen(Level level, Department department, Pageable pageable);

  List<EmployeeDTO> getEmployeeListByLevelGreaterThen(Level level);

  void deleteEmployeeById(Long id);
}
