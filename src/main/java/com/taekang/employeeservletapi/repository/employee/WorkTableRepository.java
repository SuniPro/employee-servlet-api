package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.WorkTable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkTableRepository extends JpaRepository<WorkTable, Long> {

  void deleteByEmployee_Id(Long employeeId);

  // 직원 이름 기반으로 조회
  Optional<WorkTable> findByEmployee_Name(String name);

  // 또는 ID 기반으로 조회
  Optional<WorkTable> findByEmployee_Id(Long employeeId);
}
