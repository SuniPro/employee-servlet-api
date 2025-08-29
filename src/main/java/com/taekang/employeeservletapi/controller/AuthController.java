package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.LoginRequestDTO;
import com.taekang.employeeservletapi.DTO.TokenResponse;
import com.taekang.employeeservletapi.DTO.crypto.EmployeeInfo;
import com.taekang.employeeservletapi.service.auth.AuthService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

  @Value("${server.servlet.context-path}")
  private String contextPath;

  private final JwtUtil jwtUtil;
  private final AuthService authService;

  @Autowired
  public AuthController(JwtUtil jwtUtil, AuthService authService) {
    this.jwtUtil = jwtUtil;
    this.authService = authService;
  }

  @GetMapping("check")
  public ResponseEntity<EmployeeInfo> check(@CookieValue("access-token") String token) {
    String name = jwtUtil.getEmployeeName(token);
    String department = jwtUtil.getDepartment(token);
    String site = jwtUtil.getSite(token);
    String level = jwtUtil.getLevel(token);
    return ResponseEntity.ok(new EmployeeInfo(name, department, site, level));
  }

  @PostMapping("login")
  public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDTO loginRequestDTO) {

    TokenResponse tokenResponse = authService.signIn(loginRequestDTO);

    ResponseCookie accessCookie =
        ResponseCookie.from("access-token", tokenResponse.getAccessToken())
            .httpOnly(true)
            //                    일단 프로토타입은 Http도 혀용
            //                    .secure(true)
            .path(contextPath)
            .maxAge(tokenResponse.getAccessTokenExpiresIn())
            .sameSite("Strict")
            .build();

    // Refresh Token Cookie
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
            .httpOnly(true)
            //                    일단 프로토타입은 Http도 혀용
            //                    .secure(true)
            .path(contextPath)
            .maxAge(tokenResponse.getRefreshTokenExpiresIn())
            .sameSite("Strict")
            .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
        .body(tokenResponse);
  }

  @GetMapping("logout")
  public ResponseEntity<HttpStatus> logout() {
    ResponseCookie accessCookie =
        ResponseCookie.from("access-token", "").path("/").maxAge(0).build();

    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh-token", "").path("/").maxAge(0).build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
        .body(HttpStatus.OK);
  }

  @PostMapping("/refresh")
  public ResponseEntity<Void> refresh(@CookieValue("refresh-token") String refreshToken) {
    TokenResponse tokenResponse = authService.refresh(refreshToken);

    ResponseCookie accessCookie =
            ResponseCookie.from("access-token", tokenResponse.getAccessToken())
                    .httpOnly(true)
                    //                    일단 프로토타입은 Http도 혀용
                    //                    .secure(true)
                    .path(contextPath)
                    .maxAge(tokenResponse.getAccessTokenExpiresIn())
                    .sameSite("Strict")
                    .build();

    // Refresh Token Cookie
    ResponseCookie refreshCookie =
            ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
                    .httpOnly(true)
                    //                    일단 프로토타입은 Http도 혀용
                    //                    .secure(true)
                    .path(contextPath)
                    .maxAge(tokenResponse.getRefreshTokenExpiresIn())
                    .sameSite("Strict")
                    .build();

    return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
            .build();
  }
}
