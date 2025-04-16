package com.taekang.employeeservletapi.utils;

import com.taekang.employeeservletapi.error.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    ErrorCode errorCode;
    String exception = request.getAttribute("exception").toString();

    log.debug("exception: {}", exception);

    /* 토큰이 없는 경우 */
    if (exception == null) {
      errorCode = ErrorCode.CANNOT_FIND_TOKEN;
      setResponse(response, errorCode);
    }

    /* 토큰이 만료된 경우 */
    if (exception == null) {
      errorCode = ErrorCode.TOKEN_EXPIRE;
      setResponse(response, errorCode);
    }

    /* 토큰 시크니처가 다른 경우 */
    if (exception == null) {
      errorCode = ErrorCode.TOKEN_ABNORMALITY;
      setResponse(response, errorCode);
    }
  }

  private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(errorCode.getHttpStatus().value());
    response.getWriter().println(errorCode);
  }
}
