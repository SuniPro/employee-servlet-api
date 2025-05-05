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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeServiceImplements implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AbilityRepository abilityRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public EmployeeServiceImplements(
      EmployeeRepository employeeRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      AbilityRepository abilityRepository1,
      ModelMapper modelMapper) {
    this.employeeRepository = employeeRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.abilityRepository = abilityRepository1;
    this.modelMapper = modelMapper;
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
  public Page<EmployeeDTO> getEmployeeListByLevelLessThen(Level level, Pageable pageable) {
    int rank = level.getRank();
    long totalCount = employeeRepository.countByLevelRankLessThan(rank);

    int limit = pageable.getPageSize();
    long totalPages = totalCount / limit;

    // OFFSET 값 보정
    long offset = pageable.getOffset();
    if (offset >= totalCount) {
      int lastPage = (int) Math.ceil((double) totalCount / limit) - 1;
      if (lastPage < 0) lastPage = 0;
      offset = (long) lastPage * limit;
    }

    // 1. 엔티티 리스트 조회
    List<Employee> employeeList = employeeRepository.findByLevelRankLessThan(rank, limit, offset);

    // 2. DTO로 변환
    List<EmployeeDTO> content =
        employeeList.stream()
            .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
            .collect(Collectors.toList());

    // 3. Page 객체 생성 (수정된 부분)
    return new PageImpl<>(
        content,
        PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), // 정렬 정보 유지
        totalPages);
  }

  @Override
  public Page<EmployeeDTO> getEmployeeListByLevelAndDepartmentLessThen(
      Level level, Department department, Pageable pageable) {
    Page<Employee> employeePage =
        employeeRepository.findByLevelLessThanAndDepartment(level, department, pageable);
    return employeePage.map(employee -> modelMapper.map(employee, EmployeeDTO.class));
  }

  @Override
  public List<EmployeeDTO> getEmployeeListByLevelGreaterThen(Level level) {
    List<EmployeeDTO> employeeDTOList = new ArrayList<>();
    employeeRepository
        .findByLevelGreaterThan(level)
        .forEach(employee -> employeeDTOList.add(modelMapper.map(employee, EmployeeDTO.class)));
    return employeeDTOList;
  }

  @Override
  public Employee getEmployeeByName(String name) {
    return employeeRepository.findByName(name).orElseThrow(EmployeeNotFoundException::new);
  }

  @Override
  public Page<Employee> getAllEmployees(Pageable pageable) {
    return employeeRepository.findAll(pageable);
  }

  @Override
  public void deleteEmployeeById(Long id) {
    employeeRepository.deleteById(id);
  }
}
