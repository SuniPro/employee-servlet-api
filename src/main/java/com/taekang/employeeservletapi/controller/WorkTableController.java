package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.WorkTableDTO;
import com.taekang.employeeservletapi.entity.employee.WorkMenu;
import com.taekang.employeeservletapi.service.WorkTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
public class WorkTableController {

    private final WorkTableService workTableService;

    // 🔍 직원 이름 기반 조회 (유니크하므로 가능)
    @GetMapping("/get/by/name/{name}")
    public ResponseEntity<WorkTableDTO> getByName(@PathVariable String name) {
        return ResponseEntity.ok(workTableService.getWorkTableByEmployeeName(name));
    }

    // 🔍 직원 ID 기반 조회
    @GetMapping("/get/by/id/{id}")
    public ResponseEntity<WorkTableDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workTableService.getWorkTableByEmployeeId(id));
    }

    // ✅ 등록 또는 수정
    @PostMapping("/create/{id}")
    public ResponseEntity<WorkTableDTO> createOrUpdate(
            @PathVariable Long id,
            @RequestBody List<WorkMenu> workMenuList
    ) {
        return ResponseEntity.ok(workTableService.createOrUpdateWorkTable(id, workMenuList));
    }
}
