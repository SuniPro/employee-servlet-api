package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Report;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

  Page<Report> findByEmployee_idAndInsertDateTimeBetween(
      Long employeeId,
      LocalDateTime insertDateTimeAfter,
      LocalDateTime insertDateTimeBefore,
      Pageable pageable);

  @Query(
      value =
          """
    SELECT r.*
    FROM report r
    JOIN employee e ON r.employee_id = e.id
    WHERE e.level <= :level
      AND r.insert_date_time BETWEEN :start AND :end
    ORDER BY r.insert_date_time DESC
    LIMIT :limit OFFSET :offset
""",
      nativeQuery = true)
  List<Report> findReportsByLevelWithPaging(
      @Param("level") String level,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("limit") int limit,
      @Param("offset") long offset);

  @Query(
      """
    SELECT COUNT(r)
    FROM Report r
    JOIN r.employee e
    WHERE e.level <= :level
      AND r.insertDateTime BETWEEN :start AND :end
""")
  long countReportsByMaxLevelAndPeriod(
      @Param("level") Level level,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query(
      value =
          """
    SELECT r.*
    FROM report r
    JOIN employee e ON r.employee_id = e.id
    WHERE e.level <= :level
      AND e.name = :name
      AND r.insert_date_time BETWEEN :start AND :end
    ORDER BY r.insert_date_time DESC
    LIMIT :limit OFFSET :offset
""",
      nativeQuery = true)
  List<Report> findReportsByLevelAndEmployeeNameWithPaging(
      @Param("level") String level,
      @Param("name") String name,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("limit") int limit,
      @Param("offset") long offset);

  @Query(
      """
                SELECT COUNT(r)
                FROM Report r
                JOIN r.employee e
                WHERE e.level <= :level
                AND e.name = :name
                  AND r.insertDateTime BETWEEN :start AND :end
            """)
  long countReportsByMaxLevelAndNameAndPeriod(
      @Param("level") Level level,
      @Param("name") String name,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);
}
