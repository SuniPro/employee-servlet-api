package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.WorkMenu;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkTableCreateDTO {
  private Long id;
  private List<WorkMenu> workMenuList;
}
