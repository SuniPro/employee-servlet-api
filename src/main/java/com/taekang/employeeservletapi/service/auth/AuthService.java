package com.taekang.employeeservletapi.service.auth;

import com.taekang.employeeservletapi.DTO.CustomEmployeeDTO;
import com.taekang.employeeservletapi.DTO.LoginRequestDTO;
import com.taekang.employeeservletapi.DTO.TokenResponse;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.error.EmployeeNotFoundException;
import com.taekang.employeeservletapi.error.IsNotRefreshTokenException;
import com.taekang.employeeservletapi.error.PasswordIncorrectException;
import com.taekang.employeeservletapi.error.TokenNotValidateException;
import com.taekang.employeeservletapi.repository.employee.EmployeeRepository;
import java.time.Duration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final EmployeeRepository employeeRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final RedisTemplate<String, String> redisTemplate;
  private final ModelMapper modelMapper;
  private final JwtUtil jwtUtil;

  @Value("${app.redis.refresh-token-prefix:refresh:}")
  private String refreshTokenPrefix;

  @Autowired
  public AuthService(
          EmployeeRepository employeeRepository,
          BCryptPasswordEncoder bCryptPasswordEncoder, RedisTemplate<String, String> redisTemplate,
          ModelMapper modelMapper,
          JwtUtil jwtUtil) {
    this.employeeRepository = employeeRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
      this.redisTemplate = redisTemplate;
      this.modelMapper = modelMapper;
    this.jwtUtil = jwtUtil;
  }

  /** 로그인 시: 1) 사용자 인증 2) Access/Refresh 토큰 생성 3) Redis에 Refresh 토큰 저장 4) TokenResponse 반환 */
  public TokenResponse signIn(LoginRequestDTO loginRequestDTO) {
    if (!employeeRepository.existsByName(loginRequestDTO.getName())) {
      throw new EmployeeNotFoundException();
    }

    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(loginRequestDTO.getName()).orElseThrow(EmployeeNotFoundException::new);

    if (!bCryptPasswordEncoder.matches(
        loginRequestDTO.getPassword(), employee.getPassword())) {
      throw new PasswordIncorrectException();
    }

    CustomEmployeeDTO customEmployeeDTO = modelMapper.map(employee, CustomEmployeeDTO.class);

    String accessToken = jwtUtil.createAccessToken(customEmployeeDTO);
    String refreshToken = jwtUtil.createRefreshToken(customEmployeeDTO);

    // Redis에 Refresh Token 저장 (key: refresh:{email})
    String key = refreshTokenPrefix + customEmployeeDTO.getName();
    redisTemplate
            .opsForValue()
            .set(key, refreshToken, Duration.ofSeconds(jwtUtil.getRefreshTokenExpTime()));

    return new TokenResponse(
            accessToken,
            refreshToken,
            jwtUtil.getAccessTokenExpTime(),
            jwtUtil.getRefreshTokenExpTime());
  }

  /** Refresh 토큰 재발급 */
  public TokenResponse refresh(String oldRefreshToken) {
    // 1) 토큰 타입 검증
    if (!"refresh-token".equals(jwtUtil.parseClaims(oldRefreshToken).get("type", String.class))) {
      throw new IsNotRefreshTokenException();
    }

    // 2) 사용자 식별
    String name = jwtUtil.getEmployeeName(oldRefreshToken);

    // 3) Redis 저장된 토큰 확인
    String key = refreshTokenPrefix + name;
    String saved = redisTemplate.opsForValue().get(key);
    if (saved == null || saved.equals(oldRefreshToken)) {
      throw new TokenNotValidateException();
    }
    
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);

    CustomEmployeeDTO customEmployeeDTO = modelMapper.map(employee, CustomEmployeeDTO.class);
    
    // 4) 새 토큰 생성 & Redis 교체
    String newAccess = jwtUtil.createAccessToken(customEmployeeDTO);
    String newRefresh = jwtUtil.createRefreshToken(customEmployeeDTO);
    redisTemplate
            .opsForValue()
            .set(key, newRefresh, Duration.ofSeconds(jwtUtil.getRefreshTokenExpTime()));

    return new TokenResponse(
            newAccess, newRefresh, jwtUtil.getAccessTokenExpTime(), jwtUtil.getRefreshTokenExpTime());
  }
}
