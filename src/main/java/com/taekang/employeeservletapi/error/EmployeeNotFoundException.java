package com.taekang.employeeservletapi.error;

public class EmployeeNotFoundException extends BusinessException {
  public EmployeeNotFoundException() {
    super(ErrorCode.EMPLOYEE_NOT_FOUND);
  }
}
