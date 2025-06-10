package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.PaginationResponse;
import com.taekang.employeeservletapi.DTO.ReportDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

public interface ReportService {

  ReportDTO createReport(ReportDTO reportDTO);

  ReportDTO updateReport(ReportDTO reportDTO);

  void deleteReport(Long reportId);

  PaginationResponse<ReportDTO> findReportsByLevel(
      Level level, Long employeeId, LocalDateTime start, LocalDateTime end, Pageable pageable);

  PaginationResponse<ReportDTO> findReportsByEmployeeName(
      Level level,
      Long employeeId,
      String employeeName,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable);

  PaginationResponse<ReportDTO> findReportsByDepartment(
      Level level,
      Long employeeId,
      Department department,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable);

  PaginationResponse<ReportDTO> findReportsByEmployeeNameAndDepartment(
      Level level,
      Long employeeId,
      Department department,
      String employeeName,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable);
}
