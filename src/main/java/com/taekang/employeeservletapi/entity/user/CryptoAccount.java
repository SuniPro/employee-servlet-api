package com.taekang.employeeservletapi.entity.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@Table(name = "crypto_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder(toBuilder = true)
public class CryptoAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "crypto_wallet", unique = true, nullable = false)
  private String cryptoWallet;

  @Enumerated(EnumType.STRING)
  @Column(name = "chain_type")
  private ChainType chainType;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "site")
  private String site;

  @Column(name = "memo")
  private String memo;

  @Column(name = "insert_date_time")
  private LocalDateTime insertDateTime;

  @Column(name = "update_date_time")
  private LocalDateTime updateDateTime;

  @Column(name = "delete_date_time")
  private LocalDateTime deleteDateTime;
}
