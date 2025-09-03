package com.taekang.employeeservletapi.entity.employee;

import com.taekang.employeeservletapi.entity.BaseTimeEntity;
import com.taekang.employeeservletapi.tools.converter.LevelConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Table(name = "employee")
@Builder(toBuilder = true)
public class Employee extends BaseTimeEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "department", nullable = false)
  private Department department;

  @Column(name = "site", unique = true, nullable = false)
  private String site;

  // @Enumerated를 @Convert로 대체합니다.
  @Convert(converter = LevelConverter.class)
  @Column(name = "level", nullable = false)
  private Level level;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "insert_name", nullable = false)
  private String insertName;

  @Column(name = "update_name")
  private String updateName;

  @Column(name = "delete_name")
  private String deleteName;

  @Column(name = "delete_date_time")
  private LocalDateTime deleteDateTime;
}
