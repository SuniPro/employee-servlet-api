package com.taekang.employeeservletapi.repository.user;

import com.taekang.employeeservletapi.DTO.tether.TetherAccountDTO;
import com.taekang.employeeservletapi.entity.user.TetherAccount;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TetherAccountRepository extends JpaRepository<TetherAccount, Long> {

  Optional<TetherAccount> findByTetherWallet(String tetherWallet);

  Page<TetherAccountDTO> findByEmail(String email, Pageable pageable);

  Page<TetherAccount> findByEmailContainingIgnoreCase(String email, Pageable pageable);

  @Query(
      "SELECT ta FROM TetherAccount ta WHERE LOWER(TRIM(ta.email)) LIKE LOWER(CONCAT('%',"
          + " TRIM(:email), '%'))")
  Page<TetherAccount> findByEmailContaining(@Param("email") String email, Pageable pageable);
}
