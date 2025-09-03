package com.taekang.employeeservletapi.error;

public class CannotFoundSiteException extends BusinessException {
  public CannotFoundSiteException() {
    super(ErrorCode.CANNOT_FOUND_SITE);
  }
}
