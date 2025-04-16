package com.taekang.employeeservletapi.entity.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@Table(name = "tether_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder(toBuilder = true)
public class TetherAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="tether_wallet", unique = true, nullable = false)
    private String tetherWallet;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="insert_date_time")
    private LocalDateTime insertDateTime;

    @Column(name="update_date_time")
    private LocalDateTime updateDateTime;

    @Column(name="delete_date_time")
    private LocalDateTime deleteDateTime;
}
