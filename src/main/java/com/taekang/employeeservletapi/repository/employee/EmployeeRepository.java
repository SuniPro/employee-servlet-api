package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Level;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  boolean existsById(Long id);

  boolean existsByName(String name);

  Optional<Employee> findByName(String name);

  List<Employee> findByDepartment(Department department);

  List<Employee> findByDepartmentAndName(Department department, String name);

  List<Employee> findByLevel(Level level);

  List<Employee> findByLevelAndName(Level level, String name);

  @Query(
      value =
          """
        SELECT * FROM employee
        WHERE
            CASE level
                WHEN 'STAFF' THEN 1
                WHEN 'ASSOCIATE' THEN 2
                WHEN 'SENIORMANAGER' THEN 3
                WHEN 'OFFICEMANAGER' THEN 4
                WHEN 'MANAGER' THEN 5
                WHEN 'CTO' THEN 6
                WHEN 'CDO' THEN 7
                WHEN 'CIO' THEN 8
                WHEN 'CFO' THEN 9
                WHEN 'COO' THEN 10
                WHEN 'CEO' THEN 11
            END < :rank
        ORDER BY insert_date_time DESC
        LIMIT :limit OFFSET :offset
        """,
      countQuery =
          """
            SELECT COUNT(*) FROM employee
            WHERE
                CASE level
                    WHEN 'STAFF' THEN 1
                    WHEN 'ASSOCIATE' THEN 2
                    WHEN 'SENIORMANAGER' THEN 3
                    WHEN 'OFFICEMANAGER' THEN 4
                    WHEN 'MANAGER' THEN 5
                    WHEN 'CTO' THEN 6
                    WHEN 'CDO' THEN 7
                    WHEN 'CIO' THEN 8
                    WHEN 'CFO' THEN 9
                    WHEN 'COO' THEN 10
                    WHEN 'CEO' THEN 11
                END < :rank
            """,
      nativeQuery = true)
  List<Employee> findByLevelRankLessThan(
      @Param("rank") int rank, @Param("limit") int limit, @Param("offset") long offset);

  @Query(
      value =
          """
              SELECT COUNT(*) FROM Employee employee
                          WHERE
                              CASE employee.level
                                  WHEN 'STAFF' THEN 1
                                  WHEN 'ASSOCIATE' THEN 2
                                  WHEN 'SENIORMANAGER' THEN 3
                                  WHEN 'OFFICEMANAGER' THEN 4
                                  WHEN 'MANAGER' THEN 5
                                  WHEN 'CTO' THEN 6
                                  WHEN 'CDO' THEN 7
                                  WHEN 'CIO' THEN 8
                                  WHEN 'CFO' THEN 9
                                  WHEN 'COO' THEN 10
                                  WHEN 'CEO' THEN 11
                              END < :rank
              """)
  Long countByLevelRankLessThan(@Param("rank") int rank);

  //          countByLevelRankLessThan

  List<Employee> findByLevelLessThanEqual(Level level);

  Page<Employee> findByLevelLessThanAndDepartment(
      Level level, Department department, Pageable pageable);

  List<Employee> findByLevelGreaterThan(Level level);
}
