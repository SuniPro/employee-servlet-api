package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.entity.employee.Level;
import java.util.List;

public interface ReviewService {

  void workOn(CommuteDTO commuteDTO);

  void workOff(CommuteDTO commuteDTO);

  List<WorkBalanceDTO> getWorkBalanceByEmployeeId(Long employeeId);

  void abilityReview(AbilityDTO abilityDTO);

  EmployeesAbilityDTO getEmployeeAbility(Level level, Long employeeId);

  AbilityDTO getAbilityByEmployeeId(Long id);
}
