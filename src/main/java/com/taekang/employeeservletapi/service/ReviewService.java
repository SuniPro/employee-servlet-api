package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

  void workOn(CommuteDTO commuteDTO);

  void workOff(CommuteDTO commuteDTO);

  Page<AbilityReviewDTO> getEmployeesAbilityReviews(Level level, Department department, Pageable pageable);

  List<WorkBalanceDTO> getWorkBalanceByEmployeeId(Long employeeId);

  void abilityReview(AbilityDTO abilityDTO);

  EmployeesAbilityDTO getEmployeeAbility(Level level, Long employeeId);

  AbilityDTO getAbilityByEmployeeId(Long id);

  List<String> createAbility(List<AbilityReviewDTO> abilityReviewDTOList);
}
