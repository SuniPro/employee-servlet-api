package com.taekang.employeeservletapi.error;

public class CannotFoundWalletException extends BusinessException {
  public CannotFoundWalletException() {
    super(ErrorCode.CANNOT_FOUND_WALLET);
  }
}
