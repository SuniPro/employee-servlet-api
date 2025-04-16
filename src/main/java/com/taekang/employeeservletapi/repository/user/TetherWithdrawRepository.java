package com.taekang.employeeservletapi.repository.user;

import com.taekang.employeeservletapi.entity.user.TetherWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TetherWithdrawRepository extends JpaRepository<TetherWithdraw, Long> {
}
