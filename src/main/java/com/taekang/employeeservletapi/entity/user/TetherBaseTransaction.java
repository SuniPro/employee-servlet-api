package com.taekang.employeeservletapi.entity.user;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
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
    @JoinColumn(name="tether_account_id", nullable = false)
    private TetherAccount tetherAccount;

    private BigDecimal amount;

    private Boolean accepted;

    private LocalDateTime acceptedAt;

    private LocalDateTime requestedAt;
}
