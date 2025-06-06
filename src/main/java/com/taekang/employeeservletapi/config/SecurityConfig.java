package com.taekang.employeeservletapi.config;

import com.taekang.employeeservletapi.service.auth.CustomEmployeeDetailService;
import com.taekang.employeeservletapi.service.auth.JwtAuthFilter;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import com.taekang.employeeservletapi.utils.CustomAccessDeniedHandler;
import com.taekang.employeeservletapi.utils.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private static final String[] AUTH_WHITELIST = {"/employee/get/**"};

  private final CustomEmployeeDetailService customEmployeeDetailService;
  private final JwtUtil jwtUtil;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Autowired
  public SecurityConfig(
      CustomEmployeeDetailService customEmployeeDetailService,
      JwtUtil jwtUtil,
      CustomAccessDeniedHandler customAccessDeniedHandler,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
    this.customEmployeeDetailService = customEmployeeDetailService;
    this.jwtUtil = jwtUtil;
    this.customAccessDeniedHandler = customAccessDeniedHandler;
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    httpSecurity.cors(Customizer.withDefaults());

    // 상태 관리 상태 없음으로 구성, Spring security 가 세션 생성 or 사용을 못하게 합니다.
    httpSecurity.sessionManagement(
        sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    httpSecurity.formLogin(AbstractHttpConfigurer::disable);
    httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
    httpSecurity.logout(AbstractHttpConfigurer::disable);

    httpSecurity.addFilterBefore(
        new JwtAuthFilter(customEmployeeDetailService, jwtUtil),
        UsernamePasswordAuthenticationFilter.class);

    httpSecurity.exceptionHandling(
        (exception) ->
            exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler));

    httpSecurity.authorizeHttpRequests(
        authorizeRequests ->
            // @PreAuthorization 을 사용 예정이라 모든 경로에 대한 인증처리는 일단 허가합니다.
            authorizeRequests.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().permitAll()
        //            .anyRequest().authenticated()
        );

    return httpSecurity.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
