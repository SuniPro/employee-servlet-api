package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.WorkMenu;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkTableDTO {

    private Long id;

    private String name;

    private Department department;

    private Level level;

    private List<WorkMenu> workMenuList;
}
