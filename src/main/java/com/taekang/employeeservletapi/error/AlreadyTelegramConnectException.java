package com.taekang.employeeservletapi.error;

public class AlreadyTelegramConnectException extends BusinessException {
  public AlreadyTelegramConnectException() {
    super(ErrorCode.ALREADY_TELEGRAM_CONNECT);
  }
}
