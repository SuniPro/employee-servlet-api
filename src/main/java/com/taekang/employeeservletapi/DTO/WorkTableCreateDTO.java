package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.WorkMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WorkTableCreateDTO {
  private Long id;
  private List<WorkMenu> workMenuList;
}
