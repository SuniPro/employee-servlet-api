package com.taekang.employeeservletapi.error;

public class DuplicateAccountException extends BusinessException {
    public DuplicateAccountException() {
        super(ErrorCode.DUPLICATE_ACCOUNT);
    }
}
