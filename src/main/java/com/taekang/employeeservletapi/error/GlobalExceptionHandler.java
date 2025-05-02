package com.taekang.employeeservletapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AbilityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAbilityNotFoundException(AbilityNotFoundException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(CannotDeleteException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateAccount(CannotDeleteException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(DepositNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDepositNotFoundException(DepositNotFoundException e) {

    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(DepositNotFoundOrAlreadyApprovedException.class)
  public ResponseEntity<ErrorResponse> handleDepositNotFoundOrAlreadyApprovedException(
      DepositNotFoundOrAlreadyApprovedException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(DepositVerificationException.class)
  public ResponseEntity<ErrorResponse> handleDepositVerificationException(
      DepositVerificationException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(DuplicateAccountException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateAccountException(
      DuplicateAccountException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(DuplicateEmployeeException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateEmployeeException(
      DuplicateEmployeeException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(EmployeeNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(
      EmployeeNotFoundException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(InvalidAmountException.class)
  public ResponseEntity<ErrorResponse> handleInvalidAmountException(InvalidAmountException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(NotifyNotFoundedException.class)
  public ResponseEntity<ErrorResponse> handleNotifyNotFoundedException(
      NotifyNotFoundedException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(PasswordIncorrectException.class)
  public ResponseEntity<ErrorResponse> handlePasswordIncorrectException(
      PasswordIncorrectException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(TokenNotFoundedException.class)
  public ResponseEntity<ErrorResponse> handleTokenNotFoundedException(TokenNotFoundedException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(TokenNotValidateException.class)
  public ResponseEntity<ErrorResponse> handleTokenNotValidateException(
      TokenNotValidateException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(AlreadyTetherWalletException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyTetherWalletException(
      TokenNotValidateException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  // 다른 커스텀 예외들도 이렇게 추가하면 됨
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleOther() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
