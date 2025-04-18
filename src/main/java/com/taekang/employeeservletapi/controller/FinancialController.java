package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.tether.TetherDepositAcceptedDTO;
import com.taekang.employeeservletapi.DTO.tether.TetherDepositDTO;
import com.taekang.employeeservletapi.DTO.tether.TetherWalletUpdateDTO;
import com.taekang.employeeservletapi.entity.user.TetherAccount;
import com.taekang.employeeservletapi.entity.user.TetherDeposit;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import com.taekang.employeeservletapi.service.TetherService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("financial")
public class FinancialController {

  private static final String MANAGER_ACCESS =
      "hasAnyAuthority('LEVEL_CEO', 'LEVEL_COO', 'LEVEL_CFO', 'LEVEL_CIO', 'LEVEL_CTO','LEVEL_CDO', 'LEVEL_MANAGER', 'LEVEL_OFFICEMANAGER', 'LEVEL_SENIORMANAGER')";

  private final TetherService tetherService;

  @Autowired
  public FinancialController(TetherService tetherService) {
    this.tetherService = tetherService;
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("tether/update/wallet")
  public ResponseEntity<TetherAccount> updateTetherWallet(
      @RequestBody TetherWalletUpdateDTO tetherWalletUpdateDTO) {
    return ResponseEntity.ok()
        .body(tetherService.updateTetherWallet(tetherWalletUpdateDTO.getTetherWallet()));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("tether/accept/deposit")
  public ResponseEntity<Boolean> approveDeposit(
      @RequestBody TetherDepositAcceptedDTO tetherDepositAcceptedDTO) {
    return ResponseEntity.ok().body(tetherService.depositAccept(tetherDepositAcceptedDTO));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/deposits/by/id/{id}")
  public ResponseEntity<List<TetherDeposit>> getDepositsByAccountId(@PathVariable Long id) {
    return ResponseEntity.ok().body(tetherService.getDepositsForAccount(id));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/deposits/by/status/{status}")
  public ResponseEntity<Page<TetherDepositDTO>> getDepositsByStatus(
      @PathVariable TransactionStatus status,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(tetherService.getDepositsByStatus(status, pageable));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/{status}/deposits/by/id/{id}")
  public ResponseEntity<List<TetherDeposit>> getDepositsByAccountIdAndStatus(
      @PathVariable Long id, @PathVariable TransactionStatus status) {
    return ResponseEntity.ok().body(tetherService.getApprovedDepositsForAccountById(id, status));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/approved/deposits/by/tether/wallet/{tetherWallet}")
  public ResponseEntity<List<TetherDeposit>> getApprovedDepositsByTetherWallet(
      @PathVariable String tetherWallet) {
    return ResponseEntity.ok()
        .body(tetherService.getApprovedDepositsForAccountByTetherWallet(tetherWallet));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/non/approved/deposits/by/tether/wallet/{tetherWallet}")
  public ResponseEntity<List<TetherDepositDTO>> getNonApprovedDepositsByTetherWallet(
      @PathVariable String tetherWallet) {
    return ResponseEntity.ok()
        .body(tetherService.getNonApprovedDepositsForAccountByTetherWallet(tetherWallet));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/latest/deposit/by/id/{id}")
  public ResponseEntity<TetherDeposit> getLatestDeposit(@PathVariable Long id) {
    return ResponseEntity.ok().body(tetherService.getLatestDeposit(id));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/latest/deposit/by/tether/wallet/{tetherWallet}")
  public ResponseEntity<TetherDeposit> getLatestDepositByWallet(@PathVariable String tetherWallet) {
    return ResponseEntity.ok().body(tetherService.getLatestDepositByTetherWallet(tetherWallet));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/deposits/range")
  public ResponseEntity<List<TetherDeposit>> getDepositsByDateRange(
      @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
    return ResponseEntity.ok().body(tetherService.getDepositsInRange(start, end));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/deposits/range/by/wallet/{tetherWallet}")
  public ResponseEntity<List<TetherDeposit>> getDepositsByWalletAndDateRange(
      @PathVariable String tetherWallet,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end) {
    return ResponseEntity.ok()
        .body(tetherService.getDepositsInRangeByTetherWallet(tetherWallet, start, end));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/total/deposit/amount/by/status/{status}")
  public ResponseEntity<BigDecimal> getTotalDepositAmountByStatus(
      @PathVariable TransactionStatus status) {
    return ResponseEntity.ok().body(tetherService.getTotalDepositAmountByStatus(status));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/total/deposit/amount/by/status/{status}/wallet/{tetherWallet}")
  public ResponseEntity<BigDecimal> getTotalDepositAmountByStatusAndWallet(
      @PathVariable TransactionStatus status, @PathVariable String tetherWallet) {
    return ResponseEntity.ok()
        .body(tetherService.getTotalDepositAmountByStatusAndWallet(status, tetherWallet));
  }
}
