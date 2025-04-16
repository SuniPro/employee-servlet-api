package com.taekang.employeeservletapi.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 커스텀 예외에서 전달할 ErrorCode 리스트입니다. code의 prefix는 아래와 같이 정의합니다. C = Controller, S = Service 단에서, D =
 * Database에서 발생된 에러 C의 경우는 보통 rest API 소통 같의 문제, S의 경우 database 에서 가져온 데이터의 가공 처리 중 발생한 문제 D의 경우는
 * 쓰레드 풀에서 요청한 데이터가 없을 경우로 정의된다고 판단하시면 됩니다. Code 의 숫자 나열은 HTTP 의 ERROR CODE에서 차용합니다. Code 만 받아도 이걸 본
 * 개발자는 바로 어떤 문제인지 식별되어야합니다.
 */
@Getter
public enum ErrorCode {
  EMPLOYEE_NOT_FOUND("D404", "직원 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_EMPLOYEE_NAME("C409", "중복된 이름입니다.", HttpStatus.CONFLICT),
  PASSWORD_INCORRECT("C401", "잘못된 패스워드입니다.", HttpStatus.UNAUTHORIZED),
  NAME_INCORRECT("C401", "잘못된 이름입니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_EXPIRE("C401", "이미 만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_ABNORMALITY("C401", "이미 만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_NOT_VALIDATE("C401", "인증되지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
  CANNOT_FIND_TOKEN("C401", "토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
  ABILITY_IS_EMPTY("S404", "해당 직원의 평가 기록이 없습니다.", HttpStatus.BAD_REQUEST),
  NOTIFY_NOT_FOUNDED("S404", "공지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  CANNOT_PROCESSING("C400", "처리할 수 없는 요청입니다.", HttpStatus.BAD_REQUEST),
  ACCOUNT_NOT_FOUND("D404", "계좌정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_ACCOUNT("D409", "중복된 계좌입니다.", HttpStatus.CONFLICT),
  INVALID_AMOUNT("C400", "잘못된 금액입니다.", HttpStatus.BAD_REQUEST),
  DEPOSIT_VERIFICATION("C400", "계정과 금액이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  DEPOSIT_NOT_FOUND_OR_ALREADY_APPROVED("C400", "입금기록을 찾을 수 없거나, 이미 승인되었습니다.", HttpStatus.BAD_REQUEST),
  DEPOSIT_NOT_FOUND("D404", "입금 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public String toString() {
    return "ErrorCode{" +
            "code='" + code + '\'' +
            ", message='" + message + '\'' +
            ", httpStatus=" + httpStatus +
            '}';
  }
}
