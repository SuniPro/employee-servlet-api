package com.taekang.employeeservletapi.entity.employee;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "ability")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder
public class Ability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "attitude")
    private double attitude;

    @Column(name = "creativity")
    private double creativity;

    @Column(name = "work_performance")
    private double workPerformance;

    @Column(name = "teamwork")
    private double teamwork;

    @Column(name = "knowledge_level")
    private double knowledgeLevel;

    @Column(name = "review_date")
    private LocalDate reviewDate;
}
