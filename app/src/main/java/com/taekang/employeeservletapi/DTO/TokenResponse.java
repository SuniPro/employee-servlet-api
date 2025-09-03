package com.taekang.employeeservletapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
  private final String accessToken;
  private final String refreshToken;
  private final long accessTokenExpiresIn;
  private final long refreshTokenExpiresIn;
}
