package com.taekang.employeeservletapi.service.auth;

import com.taekang.employeeservletapi.DTO.CustomEmployeeDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class JwtUtil {

  private final Key key;

  @Getter private final long accessTokenExpTime;
  @Getter private final long refreshTokenExpTime;

  public JwtUtil(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.access_token.expiration_time}") long accessTokenExpTime,
      @Value("${jwt.refresh_token.expiration_time}") long refreshTokenExpTime) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpTime = accessTokenExpTime;
    this.refreshTokenExpTime = refreshTokenExpTime;
  }

  public String createAccessToken(CustomEmployeeDTO customEmployeeDTO) {
    return createToken(customEmployeeDTO, accessTokenExpTime, "access-token");
  }

  public String createRefreshToken(CustomEmployeeDTO customEmployeeDTO) {
    return createToken(customEmployeeDTO, refreshTokenExpTime, "refresh-token");
  }

  private String createToken(CustomEmployeeDTO customEmployeeDTO, long expireTime, String type) {
    Claims claims = Jwts.claims();
    claims.put("name", customEmployeeDTO.getName());
    claims.put("department", customEmployeeDTO.getDepartment());
    claims.put("site", customEmployeeDTO.getSite());
    claims.put("level", customEmployeeDTO.getLevel());
    claims.put("type", type); // 토큰 종류를 claim에 명시

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime validUntil = now.plusSeconds(expireTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(validUntil.toInstant()))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getEmployeeName(String token) {
    return parseClaims(token).get("name", String.class);
  }

  public String getLevel(String token) {
    return parseClaims(token).get("level", String.class);
  }

  public String getDepartment(String token) {
    return parseClaims(token).get("department", String.class);
  }

  public String getSite(String token) {
    return parseClaims(token).get("site", String.class);
  }

  /**
   * JWT 검증
   *
   * @param token : 발급된 토큰입니다.
   * @return IsValidate
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  /**
   * JWT Claims 추출
   *
   * @param accessToken : 발급된 엑세스토큰입니다.
   * @return JWT Claims
   */
  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public String getAccessTokenInCookie(HttpServletRequest request) {
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
