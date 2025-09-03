package com.taekang.employeeservletapi.entity.employee;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@Table(
    name = "notify_read",
    uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "notify_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder(toBuilder = true)
public class NotifyRead {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notify_id")
  private Notify notify;

  @Column(name = "read_time")
  private LocalDateTime readTime;
}
