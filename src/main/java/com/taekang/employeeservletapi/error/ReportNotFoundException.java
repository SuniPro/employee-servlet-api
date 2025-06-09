package com.taekang.employeeservletapi.error;

public class ReportNotFoundException extends BusinessException {
  public ReportNotFoundException() {
    super(ErrorCode.REPORT_NOT_FOUND);
  }
}
