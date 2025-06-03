package com.taekang.employeeservletapi.entity.employee;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder
public class Report {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Column(name = "title", columnDefinition = "varchar(256)", nullable = false)
  private String title;

  @Column(name = "report_contents", columnDefinition = "longtext", nullable = false)
  private String reportContents;

  @Column(name = "insert_date_time", nullable = false)
  private LocalDateTime insertDateTime;

  @Column(name = "update_date_time")
  private LocalDateTime updateDateTime;
}
