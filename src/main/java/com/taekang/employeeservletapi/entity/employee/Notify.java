package com.taekang.employeeservletapi.entity.employee;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@Table(name = "notify")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder(toBuilder = true)
public class Notify {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "level", nullable = false)
  private Level level;

  @Column(name = "writer", nullable = false)
  private String writer;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "contents", nullable = false)
  private String contents;

  @Column(name = "insert_date_time", nullable = false)
  private LocalDateTime insertDateTime;

  @Column(name = "update_date_time")
  private LocalDateTime updateDateTime;

  @Column(name = "delete_date_time")
  private LocalDateTime deleteDateTime;
}
