package com.taekang.employeeservletapi.error;

public class IsNotRefreshTokenException extends BusinessException {
  public IsNotRefreshTokenException() {
    super(ErrorCode.IS_NOT_REFRESH_TOKEN);
  }
}
