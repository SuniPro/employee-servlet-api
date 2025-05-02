package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.WorkTableDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.WorkMenu;
import com.taekang.employeeservletapi.entity.employee.WorkTable;
import com.taekang.employeeservletapi.error.EmployeeNotFoundException;
import com.taekang.employeeservletapi.error.WorkTableNotFoundException;
import com.taekang.employeeservletapi.repository.employee.EmployeeRepository;
import com.taekang.employeeservletapi.repository.employee.WorkTableRepository;
import com.taekang.employeeservletapi.service.WorkTableService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkTableServiceImpl implements WorkTableService {

  private final WorkTableRepository workTableRepository;
  private final EmployeeRepository employeeRepository;

  @Autowired
  public WorkTableServiceImpl(
      WorkTableRepository workTableRepository, EmployeeRepository employeeRepository) {
    this.workTableRepository = workTableRepository;
    this.employeeRepository = employeeRepository;
  }

  /** 직원 이름은 유니크하게 관리됩니다. 이름으로 업무 탭 구성을 조회할 수 있도록 지원합니다. */
  @Override
  public WorkTableDTO getWorkTableByEmployeeName(String name) {
    WorkTable workTable =
        workTableRepository.findByEmployee_Name(name).orElseThrow(WorkTableNotFoundException::new);

    return toDTO(workTable);
  }

  @Override
  public WorkTableDTO getWorkTableByEmployeeId(Long employeeId) {
    WorkTable workTable =
        workTableRepository
            .findByEmployee_Id(employeeId)
            .orElseThrow(WorkTableNotFoundException::new);
    return toDTO(workTable);
  }

  @Override
  public WorkTableDTO createOrUpdateWorkTable(Long id, List<WorkMenu> workMenuList) {
    Employee employee = employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);

    WorkTable workTable =
        workTableRepository
            .findByEmployee_Id(id)
            .map(
                existing ->
                    existing.toBuilder()
                        .id(existing.getId())
                        .employee(existing.getEmployee())
                        .workMenuList(workMenuList)
                        .build())
            .orElse(WorkTable.builder().employee(employee).workMenuList(workMenuList).build());

    WorkTable saved = workTableRepository.save(workTable);
    return toDTO(saved);
  }

  private WorkTableDTO toDTO(WorkTable entity) {
    Employee emp = entity.getEmployee();

    return WorkTableDTO.builder()
        .id(entity.getId())
        .name(emp.getName())
        .department(emp.getDepartment())
        .level(emp.getLevel())
        .workMenuList(entity.getWorkMenuList())
        .build();
  }
}
