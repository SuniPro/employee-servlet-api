package com.taekang.employeeservletapi.service.auth;

import com.taekang.employeeservletapi.DTO.CustomEmployeeDTO;
import com.taekang.employeeservletapi.DTO.LoginRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.error.EmployeeNotFoundException;
import com.taekang.employeeservletapi.error.PasswordIncorrectException;
import com.taekang.employeeservletapi.repository.employee.EmployeeRepository;
import com.taekang.employeeservletapi.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignService {

  private final EmployeeService employeeService;
  private final EmployeeRepository employeeRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ModelMapper modelMapper;
  private final JwtUtil jwtUtil;

  @Autowired
  public SignService(
      EmployeeService employeeService,
      EmployeeRepository employeeRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      ModelMapper modelMapper,
      JwtUtil jwtUtil) {
    this.employeeService = employeeService;
    this.employeeRepository = employeeRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.modelMapper = modelMapper;
    this.jwtUtil = jwtUtil;
  }

  public String signIn(LoginRequestDTO loginRequestDTO) {

    if (!employeeRepository.existsByName(loginRequestDTO.getName())) {
      throw new EmployeeNotFoundException();
    }

    Employee employeeByName = employeeService.getEmployeeByName(loginRequestDTO.getName());

    if (!bCryptPasswordEncoder.matches(
        loginRequestDTO.getPassword(), employeeByName.getPassword())) {
      throw new PasswordIncorrectException();
    }

    CustomEmployeeDTO info = modelMapper.map(employeeByName, CustomEmployeeDTO.class);

    return jwtUtil.createAccessToken(info);
  }
}
