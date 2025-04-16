package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Level;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDTO {

  private Long id;

  private Level level;

  private String writer;

  private String title;

  private String contents;

  private LocalDateTime insertDateTime;

  private LocalDateTime updateDateTime;

  private LocalDateTime deleteDateTime;
}
