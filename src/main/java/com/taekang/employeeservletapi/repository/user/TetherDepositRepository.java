package com.taekang.employeeservletapi.repository.user;

import com.taekang.employeeservletapi.entity.user.TetherAccount;
import com.taekang.employeeservletapi.entity.user.TetherDeposit;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TetherDepositRepository extends JpaRepository<TetherDeposit, Long> {

  Optional<TetherDeposit> findById(Long id);

  Page<TetherDeposit> findByTetherAccount_EmailContainingIgnoreCaseAndStatus(
      String tetherAccountEmail, TransactionStatus status, Pageable pageable);

  @Query(
      "SELECT ta FROM TetherAccount ta WHERE LOWER(TRIM(ta.email)) LIKE LOWER(CONCAT('%',"
          + " TRIM(:email), '%'))")
  Page<TetherAccount> findByEmailContaining(@Param("email") String email, Pageable pageable);

  // 2. 계정별 전체 입금 조회 (마이페이지)
  List<TetherDeposit> findByTetherAccount_IdOrderByRequestedAtDesc(Long accountId);

  // 3. 상태 기반 조회 (예: PENDING 승인 대기 리스트)
  Page<TetherDeposit> findByStatus(TransactionStatus status, Pageable pageable);

  Page<TetherDeposit> findByStatusAndTetherAccount_Email(
      TransactionStatus status, String tetherAccountEmail, Pageable pageable);

  // 4. 계정별 + 상태 필터 조회
  List<TetherDeposit> findByTetherAccount_IdAndStatus(Long accountId, TransactionStatus status);

  // 5. 최근 입금 하나
  Optional<TetherDeposit> findTopByTetherAccountOrderByRequestedAtDesc(TetherAccount account);

  // 6. 입금 날짜 기준 범위 조회 (통계/필터링)
  List<TetherDeposit> findByRequestedAtBetween(LocalDateTime start, LocalDateTime end);

  Page<TetherDeposit> findByRequestedAtBetweenAndStatus(
      LocalDateTime start, LocalDateTime end, TransactionStatus status, Pageable pageable);

  // 7. 상태별 총 입금 합계 (native)
  @Query(
      value =
          """
                SELECT COALESCE(SUM(amount), 0)
                FROM tether_deposit
                WHERE status = :status
            """,
      nativeQuery = true)
  BigDecimal sumByStatus(@Param("status") String status); // 또는 Enum → .name()으로 변환

  // 8. 중복 검사용 ID + 상태 체크
  Optional<TetherDeposit> findByIdAndStatus(Long id, TransactionStatus status);

  @Query(
      """
              SELECT DATE(d.requestedAt), SUM(d.amount)
              FROM TetherDeposit d
              WHERE d.requestedAt BETWEEN :start AND :end
              AND d.status = :status
              GROUP BY DATE(d.requestedAt)
              ORDER BY DATE(d.requestedAt)
            """)
  List<Object[]> getDailyTotals(
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("status") TransactionStatus status);

  // 상태에 따른 특정 지갑 기준 입금 조회
  List<TetherDeposit> findByTetherAccount_TetherWalletAndStatus(
      String tetherWallet, TransactionStatus status);

  // 특정 지갑의 최근 입금 1건
  Optional<TetherDeposit> findTopByTetherAccount_TetherWalletOrderByRequestedAtDesc(
      String tetherWallet);

  Page<TetherDeposit> findByStatusAndTetherAccount_EmailAndRequestedAtBetween(
      TransactionStatus status,
      String tetherAccount_email,
      LocalDateTime requestedAtAfter,
      LocalDateTime requestedAtBefore,
      Pageable pageable);

  List<TetherDeposit> findByStatusAndRequestedAtBetween(TransactionStatus status, LocalDateTime requestedAtAfter, LocalDateTime requestedAtBefore);
  List<TetherDeposit> findByStatusAndTetherAccount_EmailAndRequestedAtBetween(TransactionStatus status, String tetherAccount_email, LocalDateTime requestedAtAfter, LocalDateTime requestedAtBefore);

  @Query(
      value =
          """
              WITH deposit_stats AS (
                  -- 전체 입금 건수 및 금액 계산 (유저 무관)
                  SELECT COUNT(*) AS total_count,
                         COALESCE(SUM(amount), 0) AS total_amount
                  FROM tether_deposit
                  WHERE status = :status
                    AND requested_at BETWEEN :start AND :end
              ),
              max_deposit_user AS (
                  -- 최대 입금자 정보 계산
                  SELECT d.tether_account_id, ta.email,
                         SUM(d.amount) AS max_amount
                  FROM tether_deposit d
                  JOIN tether_account ta ON d.tether_account_id = ta.id
                  WHERE d.status = :status
                    AND d.requested_at BETWEEN :start AND :end
                  GROUP BY d.tether_account_id, ta.email
                  ORDER BY max_amount DESC
                  LIMIT 1
              )
              -- 최종 결과 조합
              SELECT ds.total_count,
                     ds.total_amount,
                     mdu.email AS max_user_email,
                     mdu.max_amount AS max_user_amount
              FROM deposit_stats ds
                       CROSS JOIN max_deposit_user mdu;

          """,
      nativeQuery = true)
  Object[] findSummaryStatNative(
      @Param("status") String status,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query(
      value =
          """
                SELECT COALESCE(SUM(amount), 0)
                FROM tether_deposit
                WHERE status = :status
                  AND tether_account_id = (
                      SELECT id FROM tether_account WHERE tether_wallet = :wallet
                  )
            """,
      nativeQuery = true)
  BigDecimal sumByStatusAndWallet(
      @Param("status") String status, @Param("wallet") String tetherWallet);
}
