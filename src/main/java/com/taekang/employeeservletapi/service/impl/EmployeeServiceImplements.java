package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.RegisterRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Ability;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.error.DuplicateEmployeeException;
import com.taekang.employeeservletapi.error.EmployeeNotFoundException;
import com.taekang.employeeservletapi.repository.employee.AbilityRepository;
import com.taekang.employeeservletapi.repository.employee.EmployeeRepository;
import com.taekang.employeeservletapi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImplements implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AbilityRepository abilityRepository;

  @Autowired
  public EmployeeServiceImplements(
          EmployeeRepository employeeRepository,
          BCryptPasswordEncoder bCryptPasswordEncoder, AbilityRepository abilityRepository1) {
    this.employeeRepository = employeeRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
      this.abilityRepository = abilityRepository1;
  }

  @Override
  public Employee createEmployee(RegisterRequestDTO registerRequestDTO) {

    if (employeeRepository.existsByName(registerRequestDTO.getName())) {
      throw new DuplicateEmployeeException();
    }

    LocalDateTime now = LocalDateTime.now();

    Employee employee =
        Employee.builder()
            .name(registerRequestDTO.getName())
            .department(registerRequestDTO.getDepartment())
            .password(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()))
            .level(registerRequestDTO.getLevel())
            .insertName(registerRequestDTO.getInsertName())
            .insertDateTime(now)
            .build();

    Employee result = employeeRepository.save(employee);
    Ability ability =
        Ability.builder()
            .employee(result)
            .attitude(0)
            .knowledgeLevel(0)
            .teamwork(0)
            .creativity(0)
            .workPerformance(0)
            .build();

    abilityRepository.save(ability);

    return result;
  }

  @Override
  public Employee updateEmployee(EmployeeDTO employeeDTO) {

    if (!employeeRepository.existsById(employeeDTO.getId())) {
      throw new EmployeeNotFoundException();
    }

    LocalDateTime now = LocalDateTime.now();

    Employee employee =
        Employee.builder()
            .id(employeeDTO.getId())
            .name(employeeDTO.getName())
            .department(employeeDTO.getDepartment())
            .level(employeeDTO.getLevel())
            .updateName(employeeDTO.getUpdateName())
            .updateDateTime(now)
            .build();

    return employeeRepository.save(employee);
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);
  }

  @Override
  public List<Employee> getEmployeeListByDepartment(Department department) {
    return employeeRepository.findByDepartment(department);
  }

  @Override
  public List<Employee> getEmployeeListByLevel(Level level) {
    return employeeRepository.findByLevel(level);
  }

  @Override
  public Employee getEmployeeByName(String name) {
    return employeeRepository.findByName(name).orElseThrow(EmployeeNotFoundException::new);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Override
  public void deleteEmployeeById(Long id) {
    employeeRepository.deleteById(id);
  }
}
