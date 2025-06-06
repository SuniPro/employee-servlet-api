package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.LoginRequestDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.service.EmployeeService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import com.taekang.employeeservletapi.service.auth.SignService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignController {

  private final EmployeeService employeeService;
  private final JwtUtil jwtUtil;
  private final SignService signService;

  @Autowired
  public SignController(EmployeeService employeeService, JwtUtil jwtUtil, SignService signService) {
    this.employeeService = employeeService;
    this.jwtUtil = jwtUtil;
    this.signService = signService;
  }

  @GetMapping("me")
  public ResponseEntity<Employee> realMe(HttpServletRequest request) {
    String token = getAccessTokenInCookie(request);

    if (token == null) {
      ResponseEntity.status(401).body(null);
    }

    boolean validationToken = jwtUtil.validateToken(token);
    if (!validationToken) {
      ResponseEntity.status(401).body(null);
    }

    return ResponseEntity.ok()
        .body(employeeService.getEmployeeByName(jwtUtil.getEmployeeName(token)));
  }

  @PostMapping("login")
  public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {

    String token = signService.signIn(loginRequestDTO);

    ResponseCookie responseCookie = ResponseCookie.from("access-token", token).path("/").build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(token);
  }

  @GetMapping("logout")
  public ResponseEntity<HttpStatus> logout() {
    ResponseCookie responseCookie =
        ResponseCookie.from("access-token", "").path("/").maxAge(0).build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(HttpStatus.OK);
  }

  private String getAccessTokenInCookie(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals("access-token")) {
          return cookie.getValue();
        }
      }
    }
    return "";
  }
}
