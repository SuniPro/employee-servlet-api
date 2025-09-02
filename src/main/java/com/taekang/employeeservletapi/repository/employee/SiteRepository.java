package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    Optional<Site> findById(Long id);

    Optional<Site> findBySite(String site);

    List<Site> findByDeleteDateTimeIsNull();

    Page<Site> findByDeleteDateTimeIsNull(Pageable pageable);

    boolean existsBySite(String site);

    Optional<Site> findByTelegramLinkToken(String token);

    boolean existsByTelegramChatId(Long telegramChatId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update Site s
       set s.site = :newSite,
           s.updateId = :updater
     where s.id = :id
  """)
    int updateSiteName(@Param("id") Long id,
                       @Param("newSite") String newSite,
                       @Param("updater") String updater);
}
