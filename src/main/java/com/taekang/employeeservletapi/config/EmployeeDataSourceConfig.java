package com.taekang.employeeservletapi.config;

import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.taekang.employeeservletapi.repository.employee",
    entityManagerFactoryRef = "employeeEntityManagerFactory",
    transactionManagerRef = "employeeTransactionManager")
public class EmployeeDataSourceConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.employee")
  public DataSourceProperties employeeDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource employeeDataSource() {
    return employeeDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean employeeEntityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("employeeDataSource") DataSource dataSource,
      @Qualifier("jpaVendorProperties") Map<String, Object> jpaProps) {
    return builder
        .dataSource(dataSource)
        .packages("com.taekang.employeeservletapi.entity.employee")
        .persistenceUnit("employee")
        .properties(jpaProps)
        .build();
  }

  @Bean
  @Primary
  public PlatformTransactionManager employeeTransactionManager(
      @Qualifier("employeeEntityManagerFactory") EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }
}
