package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.PaginationResponse;
import com.taekang.employeeservletapi.DTO.ReportDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Report;
import com.taekang.employeeservletapi.error.ReportNotFoundException;
import com.taekang.employeeservletapi.repository.employee.ReportRepository;
import com.taekang.employeeservletapi.service.ReportService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReportServiceImplements implements ReportService {

  private final ReportRepository reportRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ReportServiceImplements(ReportRepository reportRepository, ModelMapper modelMapper) {
    this.reportRepository = reportRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  @Transactional
  public ReportDTO createReport(ReportDTO reportDTO) {
    return toReportDto(
        reportRepository.save(
            Report.builder()
                .id(reportDTO.getId())
                .employee(Employee.builder().id(reportDTO.getEmployee().getId()).build())
                .title(reportDTO.getTitle())
                .reportContents(reportDTO.getReportContents())
                .insertDateTime(reportDTO.getInsertDateTime())
                .updateDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
                .build()));
  }

  @Override
  @Transactional
  public ReportDTO updateReport(ReportDTO reportDTO) {
    return toReportDto(
        reportRepository.save(
            Report.builder()
                .id(reportDTO.getId())
                .employee(Employee.builder().id(reportDTO.getEmployee().getId()).build())
                .title(reportDTO.getTitle())
                .reportContents(reportDTO.getReportContents())
                .insertDateTime(reportDTO.getInsertDateTime())
                .updateDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
                .build()));
  }

  @Override
  public void deleteReport(Long reportId) {
    reportRepository.deleteById(reportId);
  }

  @Override
  public ReportDTO getReportById(Long reportId) {
    return reportRepository
        .findById(reportId)
        .map((report -> modelMapper.map(report, ReportDTO.class)))
        .orElseThrow(ReportNotFoundException::new);
  }

  @Override
  public PaginationResponse<ReportDTO> findReportsByLevel(
      Level level, Long employeeId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
    if (level == Level.STAFF) {
      return PaginationResponse.from(
          reportRepository
              .findByEmployee_idAndInsertDateTimeBetween(employeeId, start, end, pageable)
              .map(this::toReportDto));
    }
    Page<Report> page =
        reportRepository.findReportsByRankAndPeriodWithEmployeeFetched(
            level.getRank(), start, end, pageable);

    List<ReportDTO> dtoList = page.getContent().stream().map(this::toReportDto).toList();

    return PaginationResponse.from(new PageImpl<>(dtoList, pageable, page.getTotalElements()));
  }

  @Override
  public PaginationResponse<ReportDTO> findReportsByEmployeeName(
      Level level,
      Long employeeId,
      String employeeName,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    if (level == Level.STAFF) {
      return PaginationResponse.from(
          reportRepository
              .findByEmployee_idAndInsertDateTimeBetween(employeeId, start, end, pageable)
              .map(this::toReportDto));
    }

    Page<Report> page =
        reportRepository.findReportsByLevelAndEmployeeNameWithPaging(
            level.getRank(), employeeName, start, end, pageable);

    List<ReportDTO> dtoList = page.getContent().stream().map(this::toReportDto).toList();

    return PaginationResponse.from(new PageImpl<>(dtoList, pageable, page.getTotalElements()));
  }

  @Override
  public PaginationResponse<ReportDTO> findReportsByDepartment(
      Level level,
      Long employeeId,
      Department department,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    if (level == Level.STAFF) {
      return PaginationResponse.from(
          reportRepository
              .findByEmployee_idAndInsertDateTimeBetween(employeeId, start, end, pageable)
              .map(this::toReportDto));
    }

    Page<Report> page =
        reportRepository.findReportsByLevelAndDepartmentWithPaging(
            level.getRank(), department, start, end, pageable);

    List<ReportDTO> dtoList = page.getContent().stream().map(this::toReportDto).toList();

    return PaginationResponse.from(new PageImpl<>(dtoList, pageable, page.getTotalElements()));
  }

  @Override
  public PaginationResponse<ReportDTO> findReportsByEmployeeNameAndDepartment(
      Level level,
      Long employeeId,
      Department department,
      String employeeName,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    if (level == Level.STAFF) {
      return PaginationResponse.from(
          reportRepository
              .findByEmployee_idAndInsertDateTimeBetween(employeeId, start, end, pageable)
              .map(this::toReportDto));
    }

    Page<Report> page =
        reportRepository.findReportsByLevelAndDepartmentAndEmployeeNameWithPaging(
            level.getRank(), department, employeeName, start, end, pageable);

    List<ReportDTO> dtoList = page.getContent().stream().map(this::toReportDto).toList();

    return PaginationResponse.from(new PageImpl<>(dtoList, pageable, page.getTotalElements()));
  }

  private ReportDTO toReportDto(Report report) {
    return modelMapper.map(report, ReportDTO.class);
  }
}
