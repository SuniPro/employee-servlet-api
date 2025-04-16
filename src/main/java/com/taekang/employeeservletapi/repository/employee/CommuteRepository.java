package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Commute;
import com.taekang.employeeservletapi.entity.employee.Employee;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, Long> {

  boolean existsByEmployeeAndOnTimeBetween(
      Employee employee, LocalDateTime start, LocalDateTime end);

  boolean existsByEmployeeAndOffTimeBetween(
      Employee employee, LocalDateTime start, LocalDateTime end);

  List<Commute> findCommuteByEmployee_Id(Long employeeId);

  List<Commute> findCommuteByEmployee_IdAndOnTimeBetween(
      Long employee_id, LocalDateTime start, LocalDateTime end);

  Commute findCommuteByOffTimeBetween(LocalDateTime offTimeAfter, LocalDateTime offTimeBefore);
}
