package com.taekang.employeeservletapi.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaAuditingConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.of("administrator");
  }

  @Bean
  public DateTimeProvider auditingDateTimeProvider() {
    return () -> Optional.of(LocalDateTime.now(ZoneId.of("Asia/Seoul"))); // KST
  }
}
