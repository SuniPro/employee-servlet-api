package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Ability;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbilityRepository extends JpaRepository<Ability, Long> {

  List<Ability> findByEmployee(Employee employee);

  List<Ability> findByEmployee_Id(Long employeeId);

  List<Ability> findByEmployee_Department(Department department);

  Optional<List<Ability>> findByEmployee_IdAndReviewDate(Long employeeId, LocalDate reviewDate);

  // Employee ID 리스트와 날짜로 Ability 조회
  List<Ability> findByEmployeeIdInAndReviewDate(List<Long> employeeIds, LocalDate reviewDate);
}
