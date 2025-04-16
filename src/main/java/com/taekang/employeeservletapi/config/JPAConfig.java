package com.taekang.employeeservletapi.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class JPAConfig {

  @Bean
  @ConfigurationProperties("spring.jpa")
  public JpaProperties jpaProperties() {
    return new JpaProperties();
  }

  @Bean
  public Map<String, Object> jpaVendorProperties(JpaProperties jpaProperties) {
    return new HashMap<>(jpaProperties.getProperties());
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setShowSql(false);
    adapter.setGenerateDdl(false);
    adapter.setDatabasePlatform("org.hibernate.dialect.MariaDBDialect");
    return adapter;
  }

  @Bean
  public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
      JpaVendorAdapter jpaVendorAdapter,
      @Qualifier("jpaVendorProperties") Map<String, Object> properties,
      ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
    return new EntityManagerFactoryBuilder(
        jpaVendorAdapter, properties, persistenceUnitManager.getIfAvailable());
  }
}
