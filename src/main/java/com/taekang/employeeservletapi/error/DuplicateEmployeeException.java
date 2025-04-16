package com.taekang.employeeservletapi.error;

public class DuplicateEmployeeException extends BusinessException {
  public DuplicateEmployeeException() {
    super(ErrorCode.DUPLICATE_EMPLOYEE_NAME);
  }
}
