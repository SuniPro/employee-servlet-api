package com.taekang.employeeservletapi.error;

public class PasswordIncorrectException extends BusinessException {
  public PasswordIncorrectException() {
    super(ErrorCode.PASSWORD_INCORRECT);
  }
}
