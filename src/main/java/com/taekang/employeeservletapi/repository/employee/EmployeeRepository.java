package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsById(Long id);

    boolean existsByName(String name);

    Optional<Employee> findByName(String name);

    List<Employee> findByDepartment(Department department);

    List<Employee> findByDepartmentAndName(Department department, String name);

    List<Employee> findByLevel(Level level);

    List<Employee> findByLevelAndName(Level level, String name);

    List<Employee> findByLevelLessThanEqual(Level level);
}
