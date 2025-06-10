package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.EmployeeUpdateDTO;
import com.taekang.employeeservletapi.DTO.RegisterRequestDTO;
import com.taekang.employeeservletapi.entity.employee.*;
import com.taekang.employeeservletapi.error.DuplicateEmployeeException;
import com.taekang.employeeservletapi.error.EmployeeNotFoundException;
import com.taekang.employeeservletapi.repository.employee.*;
import com.taekang.employeeservletapi.service.EmployeeService;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EmployeeServiceImplements implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final AbilityRepository abilityRepository;
  private final CommuteRepository commuteRepository;
  private final NotifyReadRepository notifyReadRepository;
  private final ReportRepository reportRepository;
  private final WorkTableRepository workTableRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ModelMapper modelMapper;

  @Autowired
  public EmployeeServiceImplements(
      EmployeeRepository employeeRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      AbilityRepository abilityRepository1,
      CommuteRepository commuteRepository,
      NotifyReadRepository notifyReadRepository,
      ReportRepository reportRepository,
      WorkTableRepository workTableRepository,
      ModelMapper modelMapper) {
    this.employeeRepository = employeeRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.abilityRepository = abilityRepository1;
    this.commuteRepository = commuteRepository;
    this.notifyReadRepository = notifyReadRepository;
    this.reportRepository = reportRepository;
    this.workTableRepository = workTableRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public Employee createEmployee(RegisterRequestDTO registerRequestDTO) {

    if (employeeRepository.existsByName(registerRequestDTO.getName())) {
      throw new DuplicateEmployeeException();
    }

    Employee employee =
        Employee.builder()
            .name(registerRequestDTO.getName())
            .department(registerRequestDTO.getDepartment())
            .password(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()))
            .level(registerRequestDTO.getLevel())
            .insertName(registerRequestDTO.getInsertName())
            .insertDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
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
  public EmployeeDTO updateEmployee(EmployeeUpdateDTO employeeUpdateDTO) {
    Employee employee =
        employeeRepository
            .findById(employeeUpdateDTO.getId())
            .orElseThrow(EmployeeNotFoundException::new);

    if (employeeUpdateDTO.getPassword().isBlank()) {
      return toEmployeeDTO(
          employeeRepository.save(
              Employee.builder()
                  .id(employeeUpdateDTO.getId())
                  .department(employeeUpdateDTO.getDepartment())
                  .level(employeeUpdateDTO.getLevel())
                  .name(employeeUpdateDTO.getName())
                  .password(employeeUpdateDTO.getPassword())
                  .insertName(employeeUpdateDTO.getInsertName())
                  .insertDateTime(employeeUpdateDTO.getInsertDateTime())
                  .updateName(employeeUpdateDTO.getUpdateName())
                  .updateDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
                  .build()));
    }

    return toEmployeeDTO(
        employeeRepository.save(
            Employee.builder()
                .id(employeeUpdateDTO.getId())
                .department(employeeUpdateDTO.getDepartment())
                .level(employeeUpdateDTO.getLevel())
                .name(employeeUpdateDTO.getName())
                .password(bCryptPasswordEncoder.encode(employeeUpdateDTO.getPassword()))
                .insertName(employeeUpdateDTO.getInsertName())
                .insertDateTime(employeeUpdateDTO.getInsertDateTime())
                .updateName(employeeUpdateDTO.getUpdateName())
                .updateDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
                .build()));
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
  @Transactional
  public void deleteEmployeeById(Long id) {
    commuteRepository.deleteByEmployee_Id(id);
    abilityRepository.deleteByEmployee_Id(id);
    notifyReadRepository.deleteByEmployee_Id(id);
    reportRepository.deleteByEmployee_Id(id);
    workTableRepository.deleteByEmployee_Id(id);
    employeeRepository.deleteById(id);
  }

  private EmployeeDTO toEmployeeDTO(Employee employee) {
    return modelMapper.map(employee, EmployeeDTO.class);
  }
}
