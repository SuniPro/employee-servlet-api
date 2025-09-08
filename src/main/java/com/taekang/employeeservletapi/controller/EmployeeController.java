package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.EmployeeUpdateDTO;
import com.taekang.employeeservletapi.DTO.RegisterRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.service.EmployeeService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/employee")
public class EmployeeController {

  private static final String MANAGER_ACCESS =
          "hasAnyAuthority('LEVEL_DEVELOPER','LEVEL_ADMINISTRATOR','LEVEL_MANAGER')";

  private final EmployeeService employeeService;
  private final JwtUtil jwtUtil;

  @Autowired
  public EmployeeController(EmployeeService employeeService, JwtUtil jwtUtil) {
    this.employeeService = employeeService;
    this.jwtUtil = jwtUtil;
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PostMapping("create")
  public ResponseEntity<Employee> createEmployee(
      @CookieValue("access-token") String token,
      @RequestBody RegisterRequestDTO registerRequestDTO) {

    String name = jwtUtil.getEmployeeName(token);

    return ResponseEntity.ok().body(employeeService.createEmployee(name, registerRequestDTO));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PutMapping("update")
  public ResponseEntity<EmployeeDTO> updateEmployee(
          @CookieValue("access-token") String token, @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
    String name = jwtUtil.getEmployeeName(token);
    return ResponseEntity.ok().body(employeeService.updateEmployee(name, employeeUpdateDTO));
  }

  @GetMapping("get/by/{id}")
  public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
    return ResponseEntity.ok().body(employeeService.getEmployeeById(id));
  }

  @GetMapping("get/by/{name}")
  public ResponseEntity<EmployeeDTO> getEmployeeByName(@PathVariable String name) {
    return ResponseEntity.ok().body(employeeService.getEmployeeByName(name));
  }

  @GetMapping("get/by/{name}/through/list")
  public ResponseEntity<List<EmployeeDTO>> getEmployeeByNameThroughList(@PathVariable String name) {
    return ResponseEntity.ok().body(employeeService.getEmployeeByNameThroughList(name));
  }

  @GetMapping("get/all")
  public ResponseEntity<Page<EmployeeDTO>> getAllEmployeeList(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(employeeService.getAllEmployeeList(pageable));
  }

  @GetMapping("get/all/{site}")
  public ResponseEntity<Page<EmployeeDTO>> getAllEmployeeListBySite(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String site) {
    return ResponseEntity.ok().body(employeeService.getEmployeeListBySite(site, pageable));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @DeleteMapping("delete/by/{id}")
  public ResponseEntity<Long> deleteEmployee(
      @CookieValue("access-token") String token, @PathVariable Long id) {
    String name = jwtUtil.getEmployeeName(token);
    employeeService.deleteEmployeeById(name, id);

    return ResponseEntity.ok().body(id);
  }
}
