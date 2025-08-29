package com.taekang.employeeservletapi.repository.user;

import com.taekang.employeeservletapi.entity.user.CryptoAccount;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoAccountRepository extends JpaRepository<CryptoAccount, Long> {

  Optional<CryptoAccount> findByEmail(String email);

  Optional<CryptoAccount> findByCryptoWallet(String cryptoWallet);

  Page<CryptoAccount> findBySite(String site, Pageable pageable);

  Optional<CryptoAccount> findByEmailAndSite(String email, String site);

//  Page<CryptoAccount> findByEmailContainingIgnoreCase(String email, Pageable pageable);
//
//  @Query(
//      "SELECT ta FROM CryptoAccount ta WHERE LOWER(TRIM(ta.email)) LIKE LOWER(CONCAT('%',"
//          + " TRIM(:email), '%'))")
//  Page<CryptoAccount> findByEmailContaining(@Param("email") String email, Pageable pageable);
}
