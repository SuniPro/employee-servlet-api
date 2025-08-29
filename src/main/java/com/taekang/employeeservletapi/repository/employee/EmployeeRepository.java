package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  boolean existsById(Long id);

  boolean existsByName(String name);

  Optional<Employee> findByNameAndDeleteNameIsNull(String name);
  
  Page<Employee> findBySiteAndDeleteNameIsNull(String site, Pageable pageable);
  
  List<Employee> findByLevelLessThanEqualAndDeleteNameIsNull(Level level);

  Page<Employee> findAllByDeleteNameIsNull(Pageable pageable);
}
