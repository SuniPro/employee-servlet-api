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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EmployeeServiceImplements implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final SiteRepository siteRepository;
  private final SiteWalletRepository siteWalletRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ModelMapper modelMapper;

  @Autowired
  public EmployeeServiceImplements(
      EmployeeRepository employeeRepository,
      SiteRepository siteRepository,
      SiteWalletRepository siteWalletRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      ModelMapper modelMapper) {
    this.employeeRepository = employeeRepository;
    this.siteRepository = siteRepository;
    this.siteWalletRepository = siteWalletRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.modelMapper = modelMapper;
  }

  @Override
  @Transactional
  public Employee createEmployee(String name, RegisterRequestDTO registerRequestDTO) {

    if (employeeRepository.existsByName(registerRequestDTO.getName())) {
      throw new DuplicateEmployeeException();
    }

    Employee employee =
        Employee.builder()
            .department(registerRequestDTO.getDepartment())
            .level(registerRequestDTO.getLevel())
            .name(registerRequestDTO.getName())
            .password(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()))
            .site(registerRequestDTO.getSite())
            .insertName(name)
            .build();

    employeeRepository.save(employee);

    /*
     * 존재하지 않는 사이트일 경우에만 CryptoWallet이 존재함
     * */
    if (!siteRepository.existsBySite(registerRequestDTO.getSite())) {
      Site site = Site.builder().site(registerRequestDTO.getSite()).insertId(name).build();

      Site save = siteRepository.save(site);

      List<SiteWallet> list =
          registerRequestDTO.getSiteWalletList().stream()
              .map(
                  wallet ->
                      SiteWallet.builder()
                          .site(save)
                          .cryptoWallet(wallet.getCryptoWallet())
                          .chainType(wallet.getChainType())
                          .insertId(name)
                          .build())
              .toList();

      siteWalletRepository.saveAll(list);
    }

    return modelMapper.map(employee, Employee.class);
  }

  @Override
  public EmployeeDTO updateEmployee(String name, EmployeeUpdateDTO employeeUpdateDTO) {
    Employee employee =
        employeeRepository
            .findById(employeeUpdateDTO.getId())
            .orElseThrow(EmployeeNotFoundException::new);

    if (employeeUpdateDTO.getPassword().isEmpty()) {
      return toEmployeeDTO(
          employeeRepository.save(
              employee.toBuilder()
                  .id(employeeUpdateDTO.getId())
                  .department(employeeUpdateDTO.getDepartment())
                  .level(employeeUpdateDTO.getLevel())
                  .name(employeeUpdateDTO.getName())
                  .password(employee.getPassword())
                  .updateName(name)
                  .build()));
    } else {
      return toEmployeeDTO(
          employeeRepository.save(
              employee.toBuilder()
                  .id(employeeUpdateDTO.getId())
                  .department(employeeUpdateDTO.getDepartment())
                  .level(employeeUpdateDTO.getLevel())
                  .name(employeeUpdateDTO.getName())
                  .password(bCryptPasswordEncoder.encode(employeeUpdateDTO.getPassword()))
                  .updateName(name)
                  .build()));
    }
  }

  @Override
  public EmployeeDTO getEmployeeById(Long id) {
    Employee employee = employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);
    return modelMapper.map(employee, EmployeeDTO.class);
  }

  @Override
  public EmployeeDTO getEmployeeByName(String name) {
    Employee employee =
        employeeRepository
            .findByNameAndDeleteNameIsNull(name)
            .orElseThrow(EmployeeNotFoundException::new);
    return modelMapper.map(employee, EmployeeDTO.class);
  }

  @Override
  public List<EmployeeDTO> getEmployeeByNameThroughList(String name) {
    List<EmployeeDTO> employeeDTOList = new ArrayList<>();
    Employee employee =
        employeeRepository
            .findByNameAndDeleteNameIsNull(name)
            .orElseThrow(EmployeeNotFoundException::new);
    employeeDTOList.add(modelMapper.map(employee, EmployeeDTO.class));
    return employeeDTOList;
  }

  @Override
  public Page<EmployeeDTO> getAllEmployeeList(Pageable pageable) {
    return employeeRepository.findAllByDeleteNameIsNull(pageable).map(this::toEmployeeDTO);
  }

  @Override
  public Page<EmployeeDTO> getEmployeeListBySite(String site, Pageable pageable) {
    return employeeRepository
        .findBySiteAndDeleteNameIsNull(site, pageable)
        .map(this::toEmployeeDTO);
  }

  @Override
  public void deleteEmployeeById(String name, Long id) {
    Employee employee = employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);

    LocalDateTime seoulTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    employee.toBuilder().deleteName(name).deleteDateTime(seoulTime).build();
    employeeRepository.save(employee);
  }

  private EmployeeDTO toEmployeeDTO(Employee employee) {
    return modelMapper.map(employee, EmployeeDTO.class);
  }
}
