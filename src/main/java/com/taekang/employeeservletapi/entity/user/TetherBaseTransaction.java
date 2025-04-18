package com.taekang.employeeservletapi.entity.user;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TetherBaseTransaction {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tether_account_id", nullable = false)
  private TetherAccount tetherAccount;

  @Column(name = "amount")
  private BigDecimal amount;

  @Column(name = "accepted")
  private Boolean accepted;

  @Column(name = "accepted_at")
  private LocalDateTime acceptedAt;

  @Column(name = "requested_at")
  private LocalDateTime requestedAt;
}
