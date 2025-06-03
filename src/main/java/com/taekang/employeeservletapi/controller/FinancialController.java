package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.tether.*;
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
      "hasAnyAuthority('LEVEL_CEO', 'LEVEL_COO', 'LEVEL_CFO', 'LEVEL_CIO', 'LEVEL_CTO','LEVEL_CDO',"
          + " 'LEVEL_MANAGER', 'LEVEL_OFFICEMANAGER', 'LEVEL_SENIORMANAGER')";

  private final TetherService tetherService;

  @Autowired
  public FinancialController(TetherService tetherService) {
    this.tetherService = tetherService;
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/account/by/email/{email}")
  public ResponseEntity<Page<TetherAccountDTO>> getAccount(
      @PathVariable String email,
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(tetherService.getTetherAccount(email, pageable));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/account/all")
  public ResponseEntity<Page<TetherAccountDTO>> getAllAccounts(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(tetherService.getAllTetherAccount(pageable));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("tether/update/wallet")
  public ResponseEntity<TetherAccount> updateTetherWallet(
      @RequestBody TetherWalletUpdateDTO tetherWalletUpdateDTO) {
    return ResponseEntity.ok()
        .body(
            tetherService.updateTetherWallet(
                tetherWalletUpdateDTO.getId(), tetherWalletUpdateDTO.getTetherWallet()));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("tether/update/site")
  public void updateSite(@RequestBody TetherAccountUpdateDTO tetherAccountUpdateDTO) {
    tetherService.updateSite(tetherAccountUpdateDTO.getId(), tetherAccountUpdateDTO.getSite());
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("tether/update/memo")
  public void updateMemo(@RequestBody TetherAccountUpdateDTO tetherAccountUpdateDTO) {
    tetherService.updateMemo(tetherAccountUpdateDTO.getId(), tetherAccountUpdateDTO.getMemo());
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("tether/accept/deposit")
  public ResponseEntity<Boolean> approveDeposit(
      @RequestBody TetherDepositChangeStatusDTO tetherDepositChangeStatusDTO) {
    return ResponseEntity.ok().body(tetherService.depositAccept(tetherDepositChangeStatusDTO));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("tether/cancel/deposit")
  public ResponseEntity<Boolean> cancelDeposit(
      @RequestBody TetherDepositChangeStatusDTO tetherDepositChangeStatusDTO) {
    return ResponseEntity.ok().body(tetherService.depositCancel(tetherDepositChangeStatusDTO));
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
  @GetMapping("tether/get/deposits/by/status/{status}/email/{email}")
  public ResponseEntity<Page<TetherDepositDTO>> getDepositsByTetherWalletAndStatus(
      @PathVariable String email,
      @PathVariable TransactionStatus status,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok()
        .body(tetherService.getDepositsByEmailAndStatus(status, email, pageable));
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
  @DeleteMapping("tether/delete/deposit/by/id/{id}")
  public void deleteDepositById(@PathVariable Long id) {
    tetherService.deleteDepositById(id);
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
  @GetMapping("tether/get/deposits/range/by/status/{status}")
  public ResponseEntity<Page<TetherDepositDTO>> getDepositsByDateRangeAndStatus(
      @PathVariable TransactionStatus status,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok()
        .body(tetherService.getDepositsInRangeByStatus(start, end, status, pageable));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @GetMapping("tether/get/deposits/range/by/status/{status}/email/{email}")
  public ResponseEntity<Page<TetherDepositDTO>> getDepositsByStatusAndWalletAndDateRange(
      @PathVariable TransactionStatus status,
      @PathVariable String email,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok()
        .body(tetherService.getDepositsInRangeByEmail(status, email, start, end, pageable));
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
