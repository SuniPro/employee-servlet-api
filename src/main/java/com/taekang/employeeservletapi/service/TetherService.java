package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.tether.TetherAccountDTO;
import com.taekang.employeeservletapi.DTO.tether.TetherDepositAcceptedDTO;
import com.taekang.employeeservletapi.DTO.tether.TetherDepositDTO;
import com.taekang.employeeservletapi.entity.user.TetherAccount;
import com.taekang.employeeservletapi.entity.user.TetherDeposit;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TetherService {

  Page<TetherAccountDTO> getAllTetherAccount(Pageable pageable);

  TetherAccount updateTetherWallet(Long id, String tetherWallet);

  void updateSite(Long id, String site);

  void updateMemo(Long id, String memo);

  Boolean depositAccept(TetherDepositAcceptedDTO tetherDepositAcceptedDTO);

  Boolean withdrawAccept(String tetherWallet, BigDecimal amount);

  // 전체 입금내역 조회
  List<TetherDeposit> getDepositsForAccount(Long accountId);

  // 상태별 전체 리스트 조회
  Page<TetherDepositDTO> getDepositsByStatus(TransactionStatus status, Pageable pageable);

  Page<TetherDepositDTO> getDepositsByEmailAndStatus(
      TransactionStatus status, String email, Pageable pageable);

  // 특정 계정의 승인된 입금 조회
  List<TetherDeposit> getApprovedDepositsForAccountById(Long accountId, TransactionStatus status);

  // 특정 지갑의 승인된 입금 조회
  List<TetherDeposit> getApprovedDepositsForAccountByTetherWallet(String tetherWallet);

  void deleteDepositById(Long depositId);

  // 특정 지갑의 미승인된 입금 정보
  List<TetherDepositDTO> getNonApprovedDepositsForAccountByTetherWallet(String tetherWallet);

  // 특정 계좌의 최근 입금 1건 조회
  TetherDeposit getLatestDeposit(Long id);

  // 특정 지갑의 최근 입금 1건 조회
  TetherDeposit getLatestDepositByTetherWallet(String tetherWallet);

  // 특정 기간의 전체 입금 목록
  List<TetherDeposit> getDepositsInRange(LocalDateTime start, LocalDateTime end);

  Page<TetherDepositDTO> getDepositsInRangeByStatus(
      LocalDateTime start, LocalDateTime end, TransactionStatus status, Pageable pageable);

  // 특정 기간의 지갑 입금 목록
  Page<TetherDepositDTO> getDepositsInRangeByEmail(
      TransactionStatus status,
      String tetherWallet,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable);

  // 상태별 총 입금합계
  BigDecimal getTotalDepositAmountByStatus(TransactionStatus status);

  // 상태별 특정 지갑의 총 입금 합계
  BigDecimal getTotalDepositAmountByStatusAndWallet(TransactionStatus status, String tetherWallet);
}
