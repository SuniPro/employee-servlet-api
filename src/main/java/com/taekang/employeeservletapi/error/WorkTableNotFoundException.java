package com.taekang.employeeservletapi.error;

public class WorkTableNotFoundException extends BusinessException {
  public WorkTableNotFoundException() {
    super(ErrorCode.WORK_TABLE_NOT_FOUND);
  }
}
