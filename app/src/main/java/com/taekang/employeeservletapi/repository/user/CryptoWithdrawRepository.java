package com.taekang.employeeservletapi.repository.user;

import com.taekang.employeeservletapi.entity.user.CryptoWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoWithdrawRepository extends JpaRepository<CryptoWithdraw, Long> {}
