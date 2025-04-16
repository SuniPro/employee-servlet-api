package com.taekang.employeeservletapi.entity.user;

public enum TransactionStatus {
    PENDING,        // 요청
    PROCESSING,     // 지갑 처리 중
    CONFIRMED,      // 블록에 포함됨
    FAILED,         // 블록 전송 실패
    CANCELLED,      // 관리자 취소
    TIMEOUT         //시간 초과
}
