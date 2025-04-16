package com.taekang.employeeservletapi.repository.user;

import com.taekang.employeeservletapi.entity.user.TetherAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TetherAccountRepository extends JpaRepository<TetherAccount, Long> {

    Optional<TetherAccount> findByTetherWallet(String tetherWallet);
}
