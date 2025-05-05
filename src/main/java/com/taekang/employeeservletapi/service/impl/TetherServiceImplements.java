package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.tether.TetherAccountDTO;
import com.taekang.employeeservletapi.DTO.tether.TetherDepositAcceptedDTO;
import com.taekang.employeeservletapi.DTO.tether.TetherDepositDTO;
import com.taekang.employeeservletapi.entity.user.TetherAccount;
import com.taekang.employeeservletapi.entity.user.TetherDeposit;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import com.taekang.employeeservletapi.error.*;
import com.taekang.employeeservletapi.repository.user.TetherAccountRepository;
import com.taekang.employeeservletapi.repository.user.TetherDepositRepository;
import com.taekang.employeeservletapi.service.TetherService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TetherServiceImplements implements TetherService {

  private final TetherAccountRepository tetherAccountRepository;

  private final TetherDepositRepository tetherDepositRepository;

  @Autowired
  public TetherServiceImplements(
      TetherAccountRepository tetherAccountRepository,
      TetherDepositRepository tetherDepositRepository) {
    this.tetherAccountRepository = tetherAccountRepository;
    this.tetherDepositRepository = tetherDepositRepository;
  }

  @Override
  public Page<TetherAccountDTO> getAllTetherAccount(Pageable pageable) {
    return tetherAccountRepository
        .findAll(pageable)
        .map(
            tetherAccount ->
                TetherAccountDTO.builder()
                    .id(tetherAccount.getId())
                    .email(tetherAccount.getEmail())
                    .site(tetherAccount.getSite())
                    .memo(tetherAccount.getMemo())
                    .tetherWallet(tetherAccount.getTetherWallet())
                    .insertDateTime(tetherAccount.getInsertDateTime())
                    .updateDateTime(tetherAccount.getUpdateDateTime())
                    .deleteDateTime(tetherAccount.getDeleteDateTime())
                    .build());
  }

  /** tether 지갑 주소 받아 그 주소를 업데이트 합니다.. */
  @Override
  @Transactional
  public TetherAccount updateTetherWallet(Long id, String tetherWallet) {
    if (tetherAccountRepository.findByTetherWallet(tetherWallet).isEmpty()) {
      throw new AlreadyTetherWalletException();
    }

    TetherAccount tetherAccount =
        tetherAccountRepository.findById(id).orElseThrow(AccountNotFoundException::new);

    tetherAccount =
        tetherAccount.toBuilder()
            .tetherWallet(tetherWallet)
            .updateDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();
    return tetherAccountRepository.save(tetherAccount);
  }

  @Override
  public void updateSite(Long id, String site) {
    TetherAccount tetherAccount =
        tetherAccountRepository.findById(id).orElseThrow(AccountNotFoundException::new);

    tetherAccountRepository.save(tetherAccount.toBuilder().id(id).site(site).build());
  }

  @Override
  public void updateMemo(Long id, String memo) {
    TetherAccount tetherAccount =
        tetherAccountRepository.findById(id).orElseThrow(AccountNotFoundException::new);

    tetherAccountRepository.save(tetherAccount.toBuilder().id(id).memo(memo).build());
  }

  /** 입금 내역을 승인합니다. */
  @Override
  @Transactional(transactionManager = "userTransactionManager")
  public Boolean depositAccept(TetherDepositAcceptedDTO tetherDepositAcceptedDTO) {
    TetherAccount tetherAccount =
        tetherAccountRepository
            .findByTetherWallet(tetherDepositAcceptedDTO.getTetherWallet())
            .orElseThrow(AccountNotFoundException::new);

    // 2. 기존 입금 요청 확인
    TetherDeposit tetherDeposit =
        tetherDepositRepository
            .findById(tetherDepositAcceptedDTO.getDepositId())
            .orElseThrow(DepositNotFoundOrAlreadyApprovedException::new);

    // 3. 검증: 계정 & 금액 일치 여부 확인
    if (!tetherDeposit.getTetherAccount().equals(tetherAccount)
        || tetherDeposit.getAmount().compareTo(tetherDepositAcceptedDTO.getAmount()) != 0) {
      throw new DepositVerificationException(); // 커스텀 예외로 명확히
    }

    // 4. 상태 변경
    TetherDeposit updated =
        tetherDeposit.toBuilder()
            .id(tetherDeposit.getId())
            .status(TransactionStatus.CONFIRMED)
            .accepted(true)
            .acceptedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();

    tetherDepositRepository.save(updated);
    return updated.getAccepted();
  }

  @Override
  @Transactional
  public Boolean withdrawAccept(String tetherWallet, BigDecimal amount) {
    return null;
  }

  /** 모든 입금 내역을 Id를 기준으로 조회합니다. */
  @Override
  @Transactional(readOnly = true)
  public List<TetherDeposit> getDepositsForAccount(Long accountId) {
    return tetherDepositRepository.findByTetherAccount_IdOrderByRequestedAtDesc(accountId);
  }

  /** 모든 입금내역을 상태를 기준으로 조회합니다. */
  @Override
  @Transactional(transactionManager = "userTransactionManager", readOnly = true)
  public Page<TetherDepositDTO> getDepositsByStatus(TransactionStatus status, Pageable pageable) {
    return tetherDepositRepository.findByStatus(status, pageable).map(this::toTetherDepositDTO);
  }

  @Override
  @Transactional(transactionManager = "userTransactionManager", readOnly = true)
  public Page<TetherDepositDTO> getDepositsByEmailAndStatus(
      TransactionStatus status, String email, Pageable pageable) {
    return tetherDepositRepository
        .findByStatusAndTetherAccount_Email(status, email, pageable)
        .map(this::toTetherDepositDTO);
  }

  /** 아이디를 기준으로 특정 상태의 입금을 조회합니다. */
  @Override
  @Transactional(transactionManager = "userTransactionManager", readOnly = true)
  public List<TetherDeposit> getApprovedDepositsForAccountById(
      Long accountId, TransactionStatus status) {
    return tetherDepositRepository.findByTetherAccount_IdAndStatus(accountId, status);
  }

  /** 승인된 모든 입금내역을 특정 지갑을 기준으로 조회합니다. */
  @Override
  @Transactional(transactionManager = "userTransactionManager", readOnly = true)
  public List<TetherDeposit> getApprovedDepositsForAccountByTetherWallet(String tetherWallet) {
    return tetherDepositRepository.findByTetherAccount_TetherWalletAndStatus(
        tetherWallet, TransactionStatus.CONFIRMED);
  }

  /** 입금 요청 삭제 */
  @Override
  public void deleteDepositById(Long depositId) {
    try {
      tetherDepositRepository.deleteById(depositId);
    } catch (Exception e) {
      throw new CannotDeleteException();
    }
  }

  /** 미승인된 모든 입금내역을 특정 지갑을 기준으로 조회합니다. */
  @Override
  @Transactional(transactionManager = "userTransactionManager", readOnly = true)
  public List<TetherDepositDTO> getNonApprovedDepositsForAccountByTetherWallet(
      String tetherWallet) {
    return tetherDepositRepository
        .findByTetherAccount_TetherWalletAndStatus(tetherWallet, TransactionStatus.PENDING)
        .stream()
        .map(this::toTetherDepositDTO)
        .toList();
  }

  @Override
  public TetherDeposit getLatestDeposit(Long id) {
    TetherAccount tetherAccount =
        tetherAccountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    return tetherDepositRepository
        .findTopByTetherAccountOrderByRequestedAtDesc(tetherAccount)
        .orElseThrow(AccountNotFoundException::new);
  }

  /** 특정 지갑의 마지막 입금 내역을 조회합니다. */
  @Override
  @Transactional(readOnly = true)
  public TetherDeposit getLatestDepositByTetherWallet(String tetherWallet) {
    return tetherDepositRepository
        .findTopByTetherAccount_TetherWalletOrderByRequestedAtDesc(tetherWallet)
        .orElseThrow(DepositNotFoundException::new);
  }

  /** 기간을 기준으로 모든 입금 내역을 조회합니다. */
  @Override
  @Transactional(readOnly = true)
  public List<TetherDeposit> getDepositsInRange(LocalDateTime start, LocalDateTime end) {
    return tetherDepositRepository.findByRequestedAtBetween(start, end);
  }

  @Override
  @Transactional(transactionManager = "userTransactionManager", readOnly = true)
  public Page<TetherDepositDTO> getDepositsInRangeByStatus(
      LocalDateTime start, LocalDateTime end, TransactionStatus status, Pageable pageable) {
    return tetherDepositRepository
        .findByRequestedAtBetweenAndStatus(start, end, status, pageable)
        .map(this::toTetherDepositDTO);
  }

  /** 기간을 기준으로 특정 지갑의 모든 입금 목록을 조회합니다.. */
  @Override
  @Transactional(readOnly = true)
  public Page<TetherDepositDTO> getDepositsInRangeByEmail(
      TransactionStatus status,
      String email,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    return tetherDepositRepository
        .findByStatusAndTetherAccount_EmailAndRequestedAtBetween(
            status, email, start, end, pageable)
        .map(this::toTetherDepositDTO);
  }

  /** 상태별 총 입금 합계를 조회합니다. */
  @Override
  @Transactional(readOnly = true)
  public BigDecimal getTotalDepositAmountByStatus(TransactionStatus status) {
    return tetherDepositRepository.sumByStatus(status.name());
  }

  /** 특정 상태를 기준으로 그 지갑의 모든 입금 총액을 조회합니다. */
  @Override
  @Transactional(readOnly = true)
  public BigDecimal getTotalDepositAmountByStatusAndWallet(
      TransactionStatus status, String tetherWallet) {
    return tetherDepositRepository.sumByStatusAndWallet(status.name(), tetherWallet);
  }

  private TetherDepositDTO toTetherDepositDTO(TetherDeposit deposit) {

    return TetherDepositDTO.builder()
        .id(deposit.getId())
        .tetherWallet(deposit.getTetherAccount().getTetherWallet())
        .email(deposit.getTetherAccount().getEmail())
        .site(deposit.getTetherAccount().getSite())
        .memo(deposit.getTetherAccount().getMemo())
        .insertDateTime(deposit.getTetherAccount().getInsertDateTime())
        .amount(deposit.getAmount())
        .usdtAmount(deposit.getUsdtAmount())
        .accepted(deposit.getAccepted())
        .acceptedAt(deposit.getAcceptedAt())
        .requestedAt(deposit.getRequestedAt())
        .status(deposit.getStatus())
        .build();
  }
}
