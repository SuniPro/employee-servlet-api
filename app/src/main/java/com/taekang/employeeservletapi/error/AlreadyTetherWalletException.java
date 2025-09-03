package com.taekang.employeeservletapi.error;

public class AlreadyTetherWalletException extends BusinessException {
  public AlreadyTetherWalletException() {
    super(ErrorCode.ALREADY_TETHER_WALLET);
  }
}
