package com.taekang.employeeservletapi.DTO;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportDTO {

  private Long id;

  private EmployeeDTO employee;

  private String title;

  private String reportContents;

  private LocalDateTime insertDateTime;

  private LocalDateTime updateDateTime;
}
