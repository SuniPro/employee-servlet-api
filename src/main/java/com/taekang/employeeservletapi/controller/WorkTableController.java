package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.WorkTableCreateDTO;
import com.taekang.employeeservletapi.DTO.WorkTableDTO;
import com.taekang.employeeservletapi.service.WorkTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/work")
@RequiredArgsConstructor
public class WorkTableController {

  private final WorkTableService workTableService;

  @GetMapping("/get/by/name/{name}")
  public ResponseEntity<WorkTableDTO> getByName(@PathVariable String name) {
    return ResponseEntity.ok(workTableService.getWorkTableByEmployeeName(name));
  }

  @GetMapping("/get/by/id/{id}")
  public ResponseEntity<WorkTableDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(workTableService.getWorkTableByEmployeeId(id));
  }

  @PostMapping("/create")
  public ResponseEntity<WorkTableDTO> createOrUpdate(
      @RequestBody WorkTableCreateDTO workTableCreateDTO) {
    return ResponseEntity.ok(
        workTableService.createOrUpdateWorkTable(
            workTableCreateDTO.getId(), workTableCreateDTO.getWorkMenuList()));
  }
}
