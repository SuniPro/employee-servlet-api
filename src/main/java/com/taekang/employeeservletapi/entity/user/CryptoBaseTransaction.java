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
public abstract class CryptoBaseTransaction {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "crypto_account_id", nullable = false)
  private CryptoAccount cryptoAccount;

  @Enumerated(EnumType.STRING)
  @Column(name = "chain_type")
  private ChainType chainType;

  @Enumerated(EnumType.STRING)
  @Column(name = "crypto_type")
  private CryptoType cryptoType;

  @Column(name = "from_address")
  private String fromAddress;

  @Column(name = "to_address")
  private String toAddress;

  @Column(name = "amount")
  private BigDecimal amount;

  @Column(name = "krw_amount")
  private BigDecimal krwAmount;

  @Column(name = "accepted")
  private Boolean accepted;

  @Column(name = "accepted_at", nullable = true)
  private LocalDateTime acceptedAt;

  @Column(name = "requested_at")
  private LocalDateTime requestedAt;

  @Column(name = "is_send")
  private boolean isSend;
}
