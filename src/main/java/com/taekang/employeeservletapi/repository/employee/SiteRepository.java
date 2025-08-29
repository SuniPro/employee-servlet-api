package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    Optional<Site> findBySite(String site);

    List<Site> findByDeleteDateTimeIsNull();

    boolean existsBySite(String site);

    Optional<Site> findByTelegramLinkToken(String token);

    boolean existsByTelegramChatId(Long telegramChatId);
}
