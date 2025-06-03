package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.EmployeeDTO;
import com.taekang.employeeservletapi.DTO.ReportDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Report;
import com.taekang.employeeservletapi.repository.employee.ReportRepository;
import com.taekang.employeeservletapi.service.ReportService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
  public Report createReport(ReportDTO reportDTO) {
    Report report =
        Report.builder()
            .employee(Employee.builder().id(reportDTO.getEmployee().getId()).build())
            .title(reportDTO.getTitle())
            .reportContents(reportDTO.getReportContents())
            .insertDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")))
            .build();
    return reportRepository.save(report);
  }

  @Override
  public Report updateReport(ReportDTO reportDTO) {
    Report report =
        Report.builder()
            .id(reportDTO.getId())
            .employee(Employee.builder().id(reportDTO.getEmployee().getId()).build())
            .title(reportDTO.getTitle())
            .reportContents(reportDTO.getReportContents())
            .insertDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")))
            .build();
    return reportRepository.save(report);
  }

  @Override
  public void deleteReport(Long reportId) {
    reportRepository.deleteById(reportId);
  }

  @Override
  public Page<ReportDTO> findReportsByLevel(
      Level currentLevel,
      Long employeeId,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    if (currentLevel == Level.STAFF) {
      return reportRepository
          .findByEmployee_idAndInsertDateTimeBetween(employeeId, start, end, pageable)
          .map(this::toReportDto);
    }

    long totalCount = reportRepository.countReportsByMaxLevelAndPeriod(currentLevel, start, end);

    long offset = pageable.getOffset();
    int limit = pageable.getPageSize();
    if (offset >= totalCount) {
      int lastPage = (int) Math.ceil((double) totalCount / limit) - 1;
      offset = Math.max(0, lastPage * limit);
    }

    List<Report> reports =
        reportRepository.findReportsByLevelWithPaging(
            currentLevel.name(), start, end, limit, offset);

    List<ReportDTO> content =
        reports.stream()
            .map(
                r ->
                    ReportDTO.builder()
                        .id(r.getId())
                        .employee(toEmployeeDTO(r.getEmployee()))
                        .title(r.getTitle())
                        .reportContents(r.getReportContents())
                        .insertDateTime(r.getInsertDateTime())
                        .updateDateTime(r.getUpdateDateTime())
                        .build())
            .toList();

    return new PageImpl<>(content, pageable, totalCount);
  }

  @Override
  public Page<ReportDTO> findReportsByEmployeeName(
      Level level, String employeeName, LocalDateTime start, LocalDateTime end, Pageable pageable) {

    long totalCount =
        reportRepository.countReportsByMaxLevelAndNameAndPeriod(
            level, employeeName.trim(), start, end);

    long offset = pageable.getOffset();
    int limit = pageable.getPageSize();
    if (offset >= totalCount) {
      int lastPage = (int) Math.ceil((double) totalCount / limit) - 1;
      offset = Math.max(0, lastPage * limit);
    }

    List<Report> reports =
        reportRepository.findReportsByLevelAndEmployeeNameWithPaging(
            level.name(), employeeName, start, end, limit, offset);

    List<ReportDTO> content = toReportDtoList(reports);

    return new PageImpl<>(content, pageable, totalCount);
  }

  private ReportDTO toReportDto(Report report) {

    return ReportDTO.builder()
        .id(report.getId())
        .employee(toEmployeeDTO(report.getEmployee()))
        .title(report.getTitle())
        .reportContents(report.getReportContents())
        .insertDateTime(report.getInsertDateTime())
        .updateDateTime(report.getUpdateDateTime())
        .build();
  }

  private List<ReportDTO> toReportDtoList(List<Report> reports) {

    return reports.stream()
        .map(
            r ->
                ReportDTO.builder()
                    .id(r.getId())
                    .employee(toEmployeeDTO(r.getEmployee()))
                    .title(r.getTitle())
                    .reportContents(r.getReportContents())
                    .insertDateTime(r.getInsertDateTime())
                    .updateDateTime(r.getUpdateDateTime())
                    .build())
        .toList();
  }

  private EmployeeDTO toEmployeeDTO(Employee employee) {
    return modelMapper.map(employee, EmployeeDTO.class);
  }
}
