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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.taekang.employeeservletapi.repository.user",
    entityManagerFactoryRef = "userEntityManagerFactory",
    transactionManagerRef = "userTransactionManager")
public class UserDataSourceConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.user")
  public DataSourceProperties userDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource userDataSource() {
    return userDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("userDataSource") DataSource dataSource,
      @Qualifier("jpaVendorProperties") Map<String, Object> jpaProps) {
    return builder
        .dataSource(dataSource)
        .packages("com.taekang.employeeservletapi.entity.user")
        .persistenceUnit("user")
        .properties(jpaProps)
        .build();
  }

  @Bean
  public PlatformTransactionManager userTransactionManager(
      @Qualifier("userEntityManagerFactory") EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }
}
