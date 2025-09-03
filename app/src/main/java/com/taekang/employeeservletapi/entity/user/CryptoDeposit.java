package com.taekang.employeeservletapi.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "crypto_deposit")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Access(AccessType.FIELD)
public class CryptoDeposit extends CryptoBaseTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TransactionStatus status;
}
