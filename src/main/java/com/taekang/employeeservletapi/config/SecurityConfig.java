package com.taekang.employeeservletapi.config;

import com.taekang.employeeservletapi.service.auth.CustomEmployeeDetailService;
import com.taekang.employeeservletapi.service.auth.JwtAuthFilter;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import com.taekang.employeeservletapi.utils.CustomAccessDeniedHandler;
import com.taekang.employeeservletapi.utils.CustomAuthenticationEntryPoint;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  @Value("${jwt.auth.whitelist}")
  private String[] authWhitelist;

  @Value("${admin.domain}")
  private String adminDomain;

  @Value("${icoin.domain}")
  private String icoinDomain;

  private final CustomEmployeeDetailService customEmployeeDetailService;
  private final JwtUtil jwtUtil;
  private final JwtAuthFilter jwtAuthFilter;
  private final CustomAccessDeniedHandler accessDeniedHandler;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  public SecurityConfig(
      CustomEmployeeDetailService customEmployeeDetailService,
      JwtUtil jwtUtil,
      JwtAuthFilter jwtAuthFilter,
      CustomAccessDeniedHandler accessDeniedHandler,
      CustomAuthenticationEntryPoint authenticationEntryPoint) {
    this.customEmployeeDetailService = customEmployeeDetailService;
    this.jwtAuthFilter = jwtAuthFilter;
    this.accessDeniedHandler = accessDeniedHandler;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.jwtUtil = jwtUtil;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // CSRF & CORS 설정
    http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults());

    // 세션 관리 (STATELESS: 세션을 사용하지 않음)
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // 기본 로그인 방식(FormLogin, HttpBasic) 비활성화
    http.formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable);
    ;

    http.exceptionHandling(
        ex ->
            ex.authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

    // JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 앞에 배치)
    http.addFilterBefore(
        jwtAuthFilter, // 'new' 키워드로 직접 생성
        UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(
        (exception) ->
            exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

    http.authorizeHttpRequests(
        authorizeRequests ->
            authorizeRequests
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll() // CORS preflight
                .requestMatchers(authWhitelist)
                .permitAll()
                .anyRequest()
                .permitAll());

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    // 허용할 프론트 도메인
    config.setAllowedOrigins(
        List.of("https://" + adminDomain, "https://" + icoinDomain, "http://localhost:5010"));

    // 허용 HTTP 메서드
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

    // 허용 헤더
    config.setAllowedHeaders(List.of("*"));

    config.setExposedHeaders(List.of("Authorization", "Location", "Content-Type"));

    // 자격증명(Cookie, Authorization 헤더) 허용
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
