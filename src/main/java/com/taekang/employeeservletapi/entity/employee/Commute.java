package com.taekang.employeeservletapi.entity.employee;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "commute", indexes = {
        @Index(name = "idx_employee_on_time", columnList = "employee_id, on_time")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder
public class Commute {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "on_time", nullable = false)
    private LocalDateTime onTime;

    @Column(name = "off_time")
    private LocalDateTime offTime;
}
