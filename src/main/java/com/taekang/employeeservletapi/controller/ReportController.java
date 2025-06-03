package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.ReportDTO;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Report;
import com.taekang.employeeservletapi.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
  public ResponseEntity<Page<ReportDTO>> getReportsByLevel(
      @PathVariable Level level,
      @PathVariable Long employeeId,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(reportService.findReportsByLevel(level, employeeId, start, end, pageable));
  }

  @GetMapping("get/by/level/{level}/employeeName/{employeeName}/employeeId/{employeeId}")
  public ResponseEntity<Page<ReportDTO>> getReportsByLevelAndName(
      @PathVariable Level level,
      @PathVariable Long employeeName,
      @PathVariable Long employeeId,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {

    return ResponseEntity.ok()
        .body(reportService.findReportsByLevel(level, employeeId, start, end, pageable));
  }
}
