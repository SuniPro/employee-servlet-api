package com.taekang.employeeservletapi.error;

public class IsNotSupportWalletTypeException extends BusinessException {
  public IsNotSupportWalletTypeException() {
    super(ErrorCode.IS_NOT_SUPPORT_WALLET_TYPE);
  }
}
