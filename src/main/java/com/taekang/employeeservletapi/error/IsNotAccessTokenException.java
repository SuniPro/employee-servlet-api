package com.taekang.employeeservletapi.error;

public class IsNotAccessTokenException extends BusinessException {
  public IsNotAccessTokenException() {
    super(ErrorCode.IS_NOT_ACCESS_TOKEN);
  }
}
