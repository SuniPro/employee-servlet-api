package com.taekang.employeeservletapi.service.auth;

import com.taekang.employeeservletapi.DTO.CustomEmployeeDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class JwtUtil {

  private final Key key;
  private final long accessTokenExpTime;

  public JwtUtil(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.expiration_time}") long accessTokenExpTime) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpTime = accessTokenExpTime;
  }

  public String createAccessToken(CustomEmployeeDTO customEmployeeDTO) {
    return createToken(customEmployeeDTO, accessTokenExpTime);
  }

  private String createToken(CustomEmployeeDTO customEmployeeDTO, long accessTokenExpTime) {
    Claims claims = Jwts.claims();
    claims.put("name", customEmployeeDTO.getName());
    claims.put("department", customEmployeeDTO.getDepartment());
    claims.put("level", customEmployeeDTO.getLevel());

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(tokenValidity.toInstant()))
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

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty", e);
    }

    return false;
  }

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
