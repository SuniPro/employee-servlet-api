package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.RegisterRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.service.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("employee")
public class EmployeeController {

  private final EmployeeService employeeService;

  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping("create")
  public ResponseEntity<Employee> createEmployee(
      @RequestBody RegisterRequestDTO registerRequestDTO) {
    return ResponseEntity.ok().body(employeeService.createEmployee(registerRequestDTO));
  }

  @PutMapping("update")
  public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
    return ResponseEntity.ok().body(employeeService.updateEmployee(employeeDTO));
  }

  @GetMapping("get/by/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    return ResponseEntity.ok().body(employeeService.getEmployeeById(id));
  }

  @GetMapping("get/by/{name}")
  public ResponseEntity<Employee> getEmployeeByName(@PathVariable String name) {
    return ResponseEntity.ok().body(employeeService.getEmployeeByName(name));
  }

  @GetMapping("get/all")
  public ResponseEntity<Page<Employee>> getAllEmployees(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(employeeService.getAllEmployees(pageable));
  }

  @GetMapping("get/by/{department}")
  public ResponseEntity<List<Employee>> getEmployeeListByDepartment(
      @PathVariable Department department) {
    return ResponseEntity.ok().body(employeeService.getEmployeeListByDepartment(department));
  }

  @GetMapping("get/by/{level}")
  public ResponseEntity<List<Employee>> getEmployeeListByLevel(@PathVariable Level level) {
    return ResponseEntity.ok().body(employeeService.getEmployeeListByLevel(level));
  }

  @GetMapping("get/by/{level}/less/then")
  public ResponseEntity<Page<EmployeeDTO>> getEmployeeListByLevelLessThen(
      @PathVariable Level level,
      @PageableDefault(size = 10, sort = "insert_date_time", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok()
        .body(employeeService.getEmployeeListByLevelLessThen(level, pageable));
  }

  @GetMapping("get/by/{level}/department/{department}/less/then")
  public ResponseEntity<Page<EmployeeDTO>> getEmployeeListByLevelAndDepartmentLessThen(
      @PathVariable Level level,
      @PathVariable Department department,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok()
        .body(
            employeeService.getEmployeeListByLevelAndDepartmentLessThen(
                level, department, pageable));
  }

  @GetMapping("get/by/{level}/greater/then")
  public ResponseEntity<List<EmployeeDTO>> getEmployeeListByLevelGreaterThan(
      @PathVariable Level level) {
    return ResponseEntity.ok().body(employeeService.getEmployeeListByLevelGreaterThen(level));
  }

  @DeleteMapping("delete/by/{id}")
  public ResponseEntity<Long> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployeeById(id);

    return ResponseEntity.ok().body(id);
  }
}
