package com.taekang.employeeservletapi.repository.employee;

import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Notify;
import com.taekang.employeeservletapi.entity.employee.NotifyRead;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyReadRepository extends JpaRepository<NotifyRead, Long> {

  // 1. 특정 공지 + 특정 직원 → 읽었는지 여부
  Optional<NotifyRead> findByNotifyAndEmployee(Notify notify, Employee employee);

  // 2. 특정 직원이 읽은 모든 공지 목록
  List<NotifyRead> findByEmployee(Employee employee);

  // 3. 특정 공지를 읽은 모든 직원 목록
  List<NotifyRead> findByNotify(Notify notify);

  // 4. 특정 공지 읽음 여부만 boolean 체크
  boolean existsByNotifyAndEmployee(Notify notify, Employee employee);

  List<NotifyRead> findByEmployee_Id(Long employeeId);

  // (선택) 공지 + 직원 ID 조합으로도 가능
  boolean existsByNotify_IdAndEmployee_Id(Long notifyId, Long employeeId);

  @Query(
"""
    SELECT COUNT(n) FROM Notify n
    WHERE n.rank >= :rank
      AND NOT EXISTS (
        SELECT 1 FROM NotifyRead r
        WHERE r.notify = n AND r.employee.id = :employeeId
      )
""")
  long countUnreadNotifyByEmployee(@Param("employeeId") Long employeeId, @Param("rank") int rank);

  @Query("SELECT r.notify.id FROM NotifyRead r WHERE r.employee.id = :employeeId")
  List<Long> findNotifyIdsByEmployeeId(@Param("employeeId") Long employeeId);

  @Query(
      value =
          """
            SELECT n.* FROM notify n
            WHERE n.rank >= :rank
              AND EXISTS (
                SELECT 1 FROM notify_read r
                WHERE r.notify_id = n.id
                  AND r.employee_id = :employeeId
              )
            ORDER BY n.insert_date_time DESC
        """,
      nativeQuery = true)
  List<Notify> findReadNotifyListByEmployee(
      @Param("employeeId") Long employeeId, @Param("rank") int rank);

  @Query(
      value =
"""
    SELECT n.* FROM notify n
    WHERE n.rank >= :rank
      AND NOT EXISTS (
        SELECT 1 FROM notify_read r
        WHERE r.notify_id = n.id
          AND r.employee_id = :employeeId
      )
    ORDER BY n.insert_date_time DESC
""",
      nativeQuery = true)
  List<Notify> findUnreadNotifyListByEmployee(
      @Param("employeeId") Long employeeId, @Param("rank") int rank);

  @Query(
      value =
          """
              SELECT n.*
              FROM notify n
              WHERE n.id NOT IN (
                  SELECT notify_id
                  FROM notify_read
                  WHERE employee_id = :employeeId
              )
              ORDER BY n.insert_date_time DESC
          """,
      nativeQuery = true)
  List<Notify> findUnreadNotifyAllLevelByEmployee(@Param("employeeId") Long employeeId);
}
