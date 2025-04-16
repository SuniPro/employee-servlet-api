package com.taekang.employeeservletapi.error;

public class InvalidAmountException extends BusinessException {
    public InvalidAmountException() {
        super(ErrorCode.INVALID_AMOUNT);
    }
}
