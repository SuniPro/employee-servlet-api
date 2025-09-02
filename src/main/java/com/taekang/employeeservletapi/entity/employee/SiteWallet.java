package com.taekang.employeeservletapi.entity.employee;

import com.taekang.employeeservletapi.entity.BaseTimeEntity;
import com.taekang.employeeservletapi.entity.user.ChainType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Table(name = "site_wallet")
@Builder(toBuilder = true)
public class SiteWallet extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "site_id")
  private Site site;

  @Column(name = "crypto_wallet", nullable = false)
  private String cryptoWallet;

  @Enumerated(EnumType.STRING)
  @Column(name = "chain_type")
  private ChainType chainType;

  @Column(name = "insert_id", nullable = false)
  private String insertId;

  @Column(name = "update_id")
  private String updateId;

  @Column(name = "delete_id")
  private String deleteId;

  @Column(name = "delete_date_time")
  private LocalDateTime deleteDateTime;
}
