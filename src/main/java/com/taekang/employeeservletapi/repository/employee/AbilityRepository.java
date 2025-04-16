package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Ability;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbilityRepository extends JpaRepository<Ability, Long> {

  List<Ability> findByEmployee(Employee employee);

  List<Ability> findByEmployee_Id(Long employeeId);

  List<Ability> findByEmployee_Department(Department department);
}
