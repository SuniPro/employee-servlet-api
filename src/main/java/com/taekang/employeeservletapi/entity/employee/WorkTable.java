package com.taekang.employeeservletapi.entity.employee;

import com.taekang.employeeservletapi.tools.converter.WorkMenuListConverter;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Getter
@Entity
@Table(name = "work_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder(toBuilder = true)
public class WorkTable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Convert(converter = WorkMenuListConverter.class)
  @Column(name = "work_menu_string", nullable = false, length = 255)
  private List<WorkMenu> workMenuList;
}
