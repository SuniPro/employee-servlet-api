package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.EmployeeUpdateDTO;
import com.taekang.employeeservletapi.DTO.RegisterRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

  Employee createEmployee(String name, RegisterRequestDTO registerRequestDTO);

  EmployeeDTO updateEmployee(EmployeeUpdateDTO employeeUpdateDTO);

  EmployeeDTO getEmployeeById(Long id);

  EmployeeDTO getEmployeeByName(String name);

  List<EmployeeDTO> getEmployeeByNameThroughList(String name);

  Page<EmployeeDTO> getAllEmployeeList(Pageable pageable);

  Page<EmployeeDTO> getEmployeeListBySite(String site, Pageable pageable);

//  Page<EmployeeDTO> getEmployeeListByLevel(Level level);

  void deleteEmployeeById(String name, Long id);
}
