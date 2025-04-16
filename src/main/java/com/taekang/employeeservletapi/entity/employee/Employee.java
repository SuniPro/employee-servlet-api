package com.taekang.employeeservletapi.entity.employee;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "insert_name", nullable = false)
    private String insertName;

    @Column(name = "insert_date_time", nullable = false)
    private LocalDateTime insertDateTime;

    @Column(name = "update_name")
    private String updateName;

    @Column(name = "update_date_time")
    private LocalDateTime updateDateTime;

    @Column(name = "delete_name")
    private String deleteName;

    @Column(name = "delete_date_time")
    private LocalDateTime deleteDateTime;
}
