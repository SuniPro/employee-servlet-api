package com.taekang.employeeservletapi.error;

public class DepositNotFoundOrAlreadyApprovedException extends BusinessException {
  public DepositNotFoundOrAlreadyApprovedException() {
    super(ErrorCode.DEPOSIT_NOT_FOUND_OR_ALREADY_APPROVED);
  }
}
