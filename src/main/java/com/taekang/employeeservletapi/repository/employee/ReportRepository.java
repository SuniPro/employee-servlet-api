package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Department;
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
    WHERE e.rank <= :rank
      AND r.insert_date_time BETWEEN :start AND :end
    ORDER BY r.insert_date_time DESC
    LIMIT :limit OFFSET :offset
""",
      nativeQuery = true)
  List<Report> findReportsByLevelWithPaging(
      @Param("rank") int rank,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("limit") int limit,
      @Param("offset") long offset);

  @Query(
      """
    SELECT COUNT(r)
    FROM Report r
    JOIN r.employee e
    WHERE e.rank <= :rank
      AND r.insertDateTime BETWEEN :start AND :end
""")
  long countReportsByMaxLevelAndPeriod(
      @Param("rank") int rank,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query(
      value =
          """
            SELECT r.*
            FROM report r
            JOIN employee e ON r.employee_id = e.id
            WHERE e.rank <= :rank
              AND e.name = :name
              AND r.insert_date_time BETWEEN :start AND :end
            ORDER BY r.insert_date_time DESC
            LIMIT :limit OFFSET :offset
        """,
      nativeQuery = true)
  List<Report> findReportsByLevelAndEmployeeNameWithPaging(
      @Param("rank") int rank,
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
                    WHERE e.rank <= :rank
                    AND e.name = :name
                      AND r.insertDateTime BETWEEN :start AND :end
                """)
  long countReportsByMaxLevelAndNameAndPeriod(
      @Param("rank") int rank,
      @Param("name") String name,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query(
      value =
          """
            SELECT r.*
            FROM report r
            JOIN employee e ON r.employee_id = e.id
            WHERE e.rank <= :rank
              AND e.department = :department
              AND r.insert_date_time BETWEEN :start AND :end
            ORDER BY r.insert_date_time DESC
            LIMIT :limit OFFSET :offset
        """,
      nativeQuery = true)
  List<Report> findReportsByLevelAndDepartmentWithPaging(
      @Param("rank") int rank,
      @Param("department") Department department,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("limit") int limit,
      @Param("offset") long offset);

  @Query(
      value =
          """
    SELECT r.*
    FROM report r
    JOIN employee e ON r.employee_id = e.id
    WHERE e.rank <= :rank
      AND e.department = :department
      AND e.name = :name
      AND r.insert_date_time BETWEEN :start AND :end
    ORDER BY r.insert_date_time DESC
    LIMIT :limit OFFSET :offset
""",
      nativeQuery = true)
  List<Report> findReportsByLevelAndDepartmentAndEmployeeNameWithPaging(
      @Param("rank") int rank,
      @Param("department") Department department,
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
