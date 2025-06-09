package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Report;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

  Page<Report> findByEmployee_idAndInsertDateTimeBetween(
      Long employeeId,
      LocalDateTime insertDateTimeAfter,
      LocalDateTime insertDateTimeBefore,
      Pageable pageable);

  @EntityGraph(attributePaths = {"employee"})
  @Query(
      """
        SELECT r
        FROM Report r
        WHERE r.employee.rank <= :rank
          AND r.insertDateTime BETWEEN :start AND :end
        ORDER BY r.insertDateTime DESC
    """)
  Page<Report> findReportsByRankAndPeriodWithEmployeeFetched(
      @Param("rank") int rank,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      Pageable pageable);

  @EntityGraph(attributePaths = {"employee"})
  @Query(
      value =
          """
            SELECT r
            FROM Report r
            WHERE r.employee.rank <= :rank
              AND r.employee.name = :name
              AND r.insertDateTime BETWEEN :start AND :end
            ORDER BY r.insertDateTime DESC
        """)
  Page<Report> findReportsByLevelAndEmployeeNameWithPaging(
      @Param("rank") int rank,
      @Param("name") String name,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      Pageable pageable);

  @EntityGraph(attributePaths = {"employee"})
  @Query(
      value =
          """
                    SELECT r
                    FROM Report r
                    WHERE r.employee.rank <= :rank
                      AND r.employee.department = :department
                      AND r.insertDateTime BETWEEN :start AND :end
                    ORDER BY r.insertDateTime DESC
                """)
  Page<Report> findReportsByLevelAndDepartmentWithPaging(
      @Param("rank") int rank,
      @Param("department") Department department,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      Pageable pageable);

  @EntityGraph(attributePaths = {"employee"})
  @Query(
      value =
          """
                    SELECT r
                    FROM Report r
                    WHERE r.employee.rank <= :rank
                      AND r.employee.department = :department
                      AND r.employee.name = :name
                      AND r.insertDateTime BETWEEN :start AND :end
                    ORDER BY r.insertDateTime DESC
                """)
  Page<Report> findReportsByLevelAndDepartmentAndEmployeeNameWithPaging(
      @Param("rank") int rank,
      @Param("department") Department department,
      @Param("name") String name,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      Pageable pageable);

  @Query(
      """
                    SELECT COUNT(r)
                    FROM Report r
                    JOIN r.employee e
                    WHERE e.rank <= :rank
                    AND e.department = :department
                      AND r.insertDateTime BETWEEN :start AND :end
                """)
  long countReportsByMaxLevelAndDepartmentAndPeriod(
      @Param("rank") int rank,
      @Param("department") Department department,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query(
      """
                    SELECT COUNT(r)
                    FROM Report r
                    JOIN r.employee e
                    WHERE e.rank <= :rank
                    AND e.name = :name
                    AND e.department = :department
                      AND r.insertDateTime BETWEEN :start AND :end
                """)
  long countReportsByMaxLevelAndNameNadDepartmentAndPeriod(
      @Param("rank") int rank,
      @Param("name") String name,
      @Param("department") Department department,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);
}
