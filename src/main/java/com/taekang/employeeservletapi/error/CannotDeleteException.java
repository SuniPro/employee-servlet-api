package com.taekang.employeeservletapi.error;

public class CannotDeleteException extends BusinessException {
  public CannotDeleteException() {
    super(ErrorCode.CANNOT_DELETE);
  }
}
