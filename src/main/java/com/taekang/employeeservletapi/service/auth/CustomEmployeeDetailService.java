package com.taekang.employeeservletapi.service.auth;

import com.taekang.employeeservletapi.DTO.CustomEmployeeDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.repository.employee.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomEmployeeDetailService implements UserDetailsService {

  private final EmployeeRepository employeeRepository;

  private final ModelMapper modelMapper;

  public CustomEmployeeDetailService(
      EmployeeRepository employeeRepository, ModelMapper modelMapper) {
    this.employeeRepository = employeeRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    Employee employee =
        employeeRepository
            .findByName(name)
            .orElseThrow(() -> new UsernameNotFoundException(name + "는 없는 직원입니다."));

    CustomEmployeeDTO dto = modelMapper.map(employee, CustomEmployeeDTO.class);

    return new CustomEmployeeDetails(dto);
  }
}
