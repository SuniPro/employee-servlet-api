package com.taekang.employeeservletapi.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  private String code;
  private String message;
  private HttpStatus httpStatus;

  public ErrorResponse(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
    this.httpStatus = errorCode.getHttpStatus();
  }
}
