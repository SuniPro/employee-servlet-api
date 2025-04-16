package com.taekang.employeeservletapi.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "tether_withdraw")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder
public class TetherWithdraw extends TetherBaseTransaction {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private TransactionStatus status;
}
