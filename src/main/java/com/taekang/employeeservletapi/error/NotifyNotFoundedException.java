package com.taekang.employeeservletapi.error;

public class NotifyNotFoundedException extends BusinessException {
  public NotifyNotFoundedException() {
    super(ErrorCode.NOTIFY_NOT_FOUNDED);
  }
}
