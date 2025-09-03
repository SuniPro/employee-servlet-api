package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Notify;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
  List<Notify> findByLevelGreaterThanEqualAndSite(Level levelIsGreaterThan,String site);

  Optional<Notify> findTopByOrderByInsertDateTimeDesc();
}
