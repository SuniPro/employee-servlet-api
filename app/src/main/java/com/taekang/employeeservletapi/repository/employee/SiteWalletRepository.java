package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Site;
import com.taekang.employeeservletapi.entity.employee.SiteWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SiteWalletRepository extends JpaRepository<SiteWallet, Integer> {
    Optional<SiteWallet> findById(Long id);
    Optional<SiteWallet> findByCryptoWallet(String cryptoWallet);

    Optional<List<SiteWallet>> findBySite(Site site);

//    findBySite_Id

    List<SiteWallet> findBySite_IdIn(Collection<Long> siteIds);

    List<SiteWallet> findBySite_Id(Long siteId);

    void deleteBySite_Id(Long siteId);
}
