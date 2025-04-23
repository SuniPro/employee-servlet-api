package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.WorkTableDTO;
import com.taekang.employeeservletapi.entity.employee.WorkMenu;

import java.util.List;

public interface WorkTableService {

    WorkTableDTO getWorkTableByEmployeeName(String employeeName);

    WorkTableDTO getWorkTableByEmployeeId(Long id);

    WorkTableDTO createOrUpdateWorkTable(Long id, List<WorkMenu> workMenuList);
}
