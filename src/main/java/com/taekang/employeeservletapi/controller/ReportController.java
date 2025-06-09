package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.PaginationResponse;
import com.taekang.employeeservletapi.DTO.ReportDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Report;
import com.taekang.employeeservletapi.service.ReportService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("report")
public class ReportController {

  private final ReportService reportService;

  @Autowired
  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @PostMapping("create")
  public ResponseEntity<Report> createReport(@RequestBody ReportDTO reportDTO) {
    return ResponseEntity.ok().body(reportService.createReport(reportDTO));
  }

  @PutMapping("update")
  public ResponseEntity<Report> updateReport(@RequestBody ReportDTO reportDTO) {
    return ResponseEntity.ok().body(reportService.updateReport(reportDTO));
  }

  @DeleteMapping("delete/{reportId}")
  public void deleteReport(@PathVariable Long reportId) {
    reportService.deleteReport(reportId);
  }

  @GetMapping("get/by/level/{level}/employeeId/{employeeId}")
  public ResponseEntity<PaginationResponse<ReportDTO>> getReportsByLevel(
      @PathVariable Level level,
      @PathVariable Long employeeId,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(reportService.findReportsByLevel(level, employeeId, start, end, pageable));
  }

  @GetMapping("get/by/level/{level}/employeeId/{employeeId}/employeeName/{employeeName}")
  public ResponseEntity<PaginationResponse<ReportDTO>> getReportsByLevelAndName(
      @PathVariable Level level,
      @PathVariable Long employeeId,
      @PathVariable String employeeName,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(
            reportService.findReportsByEmployeeName(
                level, employeeId, employeeName, start, end, pageable));
  }

  @GetMapping("get/by/level/{level}/employeeId/{employeeId}/department/{department}")
  public ResponseEntity<PaginationResponse<ReportDTO>> getReportsByLevelAndDepartment(
      @PathVariable Level level,
      @PathVariable Long employeeId,
      @PathVariable Department department,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(
            reportService.findReportsByDepartment(
                level, employeeId, department, start, end, pageable));
  }

  @GetMapping(
      "get/by/level/{level}/employeeId/{employeeId}/employeeName/{employeeName}/department/{department}")
  public ResponseEntity<PaginationResponse<ReportDTO>> getReportsByLevelAndNameAndDepartment(
      @PathVariable Level level,
      @PathVariable Long employeeId,
      @PathVariable Department department,
      @PathVariable String employeeName,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(
            reportService.findReportsByEmployeeNameAndDepartment(
                level, employeeId, department, employeeName, start, end, pageable));
  }
}
