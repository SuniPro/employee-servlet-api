package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.ReportDTO;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReportService {

    Report createReport(ReportDTO reportDTO);

    Report updateReport(ReportDTO reportDTO);

    void deleteReport(Long reportId);


    Page<ReportDTO> findReportsByLevel(Level level, Long employeeId,
                                       LocalDateTime start, LocalDateTime end,
                                       Pageable pageable);

    Page<ReportDTO> findReportsByEmployeeName(Level level, String employeeName, LocalDateTime start,
                                              LocalDateTime end, Pageable pageable);
}
