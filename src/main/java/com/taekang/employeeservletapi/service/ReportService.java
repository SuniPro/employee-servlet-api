package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.ReportDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Report;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

  Report createReport(ReportDTO reportDTO);

  Report updateReport(ReportDTO reportDTO);

  void deleteReport(Long reportId);

  Page<ReportDTO> findReportsByLevel(
      Level level, Long employeeId, LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<ReportDTO> findReportsByEmployeeName(
          Level level, Long employeeId, String employeeName, LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<ReportDTO> findReportsByDepartment(
          Level level, Long employeeId, Department department, LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<ReportDTO> findReportsByEmployeeNameAndDepartment(
          Level level, Long employeeId, Department department, String employeeName, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
