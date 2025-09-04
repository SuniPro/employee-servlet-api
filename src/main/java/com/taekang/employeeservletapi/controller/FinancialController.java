package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.DTO.crypto.*;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import com.taekang.employeeservletapi.service.CryptoService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
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
@RequestMapping("/employee/financial")
public class FinancialController {

  private static final String DEFAULT_ACCESS =
      "hasAnyAuthority('LEVEL_ADMINISTRATOR','LEVEL_MANAGER', 'LEVEL_OFFICEMANAGER',"
          + " 'LEVEL_SENIORMANAGER')";

  private static final String MANAGER_ACCESS =
      "hasAnyAuthority('LEVEL_ADMINISTRATOR','LEVEL_MANAGER')";

  private final CryptoService cryptoService;
  private final JwtUtil jwtUtil;

  @Autowired
  public FinancialController(CryptoService cryptoService, JwtUtil jwtUtil) {
    this.cryptoService = cryptoService;
    this.jwtUtil = jwtUtil;
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/account")
  public ResponseEntity<Page<CryptoAccountDTO>> getAllCryptoAccount(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(cryptoService.getCryptoAccountList(pageable));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/account/email/{email}")
  public ResponseEntity<List<CryptoAccountDTO>> getCryptoAccountByEmail(
      @PathVariable String email) {
    return ResponseEntity.ok().body(cryptoService.getCryptoAccountByEmail(email));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/deposit/wallet/{cryptoWallet}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByCryptoWallet(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String cryptoWallet) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsByCryptoWallet(cryptoWallet, pageable));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/deposit/status/{status}/address/to/{address}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByToAddressAndStatus(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable TransactionStatus status) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsByToAddressAndStatus(status, address, pageable));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/deposit/status/{status}/address/from/{address}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByFromAddressAndStatus(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable TransactionStatus status) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsByFromAddressAndStatus(status, address, pageable));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/deposit/range")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsInRange(
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(cryptoService.getDepositsInRange(start, end, pageable));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/deposit/range/address/to/{address}/send/{isSend}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsInRangeByToAddressAndIsSend(
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable boolean isSend) {
    return ResponseEntity.ok()
        .body(
            cryptoService.getDepositsInRangeByToAddressAndIsSend(
                isSend, address, start, end, pageable));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @GetMapping("get/crypto/deposit/range/address/from/{address}/send/{isSend}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsInRangeByFromAddressAndIsSend(
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable boolean isSend) {
    return ResponseEntity.ok()
        .body(
            cryptoService.getDepositsInRangeByFromAddressAndIsSend(
                isSend, address, start, end, pageable));
  }

  @PreAuthorize("hasAnyAuthority('LEVEL_ADMINISTRATOR')")
  @DeleteMapping("delete/deposit/{id}")
  public void deleteDeposit(@PathVariable Long id) {
    cryptoService.deleteDepositById(id);
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PatchMapping("update/site/{site}/by/{id}")
  public void updateSite(@RequestBody UpdateSiteDTO updateSiteDTO) {
    cryptoService.updateSite(updateSiteDTO);
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @PatchMapping("update/memo")
  public void updateMemo(@RequestBody UpdateMemoDTO updateMemoDTO) {
    cryptoService.updateMemo(updateMemoDTO);
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @PatchMapping("update/send")
  public ResponseEntity<Boolean> updateSend(@RequestBody UpdateIsSendDTO updateIsSendDTO) {
    return ResponseEntity.ok().body(cryptoService.updateSend(updateIsSendDTO));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  // cryptoWallet 이라고 적어야하지만 CamelCase는 url에서 권장되지 않기에 wallet으로 명명함.
  @PatchMapping("update/wallet")
  public ResponseEntity<CryptoAccountDTO> updateCryptoWallet(
      @RequestBody UpdateCryptoWalletDTO updateCryptoWalletDTO) {
    return ResponseEntity.ok().body(cryptoService.updateCryptoWallet(updateCryptoWalletDTO));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/account/by/site/{site}")
  public ResponseEntity<Page<CryptoAccountDTO>> getAllCryptoAccountBySite(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String site) {
    return ResponseEntity.ok().body(cryptoService.getCryptoAccountListBySite(site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/account/email/{email}/by/site/{site}")
  public ResponseEntity<List<CryptoAccountDTO>> getCryptoAccountByEmailAndSite(
      @PathVariable String email, @PathVariable String site) {
    return ResponseEntity.ok().body(cryptoService.getCryptoAccountByEmailAndSite(email, site));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/account/wallet/{cryptoWallet}")
  public ResponseEntity<CryptoAccountDTO> getCryptoAccountByCryptoWallet(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String cryptoWallet) {
    return ResponseEntity.ok()
        .body(cryptoService.getCryptoAccountByCryptoWallet(cryptoWallet, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsBySite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String site) {
    return ResponseEntity.ok().body(cryptoService.getDepositsBySite(site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/send/{send}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getSendDepositsBySite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String site,
      @PathVariable boolean send) {
    return ResponseEntity.ok().body(cryptoService.getSendDepositsBySite(site, send, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/email/{email}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByEmailAndSite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String email,
      @PathVariable String site) {
    return ResponseEntity.ok().body(cryptoService.getDepositsByEmailAndSite(email, site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/wallet/{cryptoWallet}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByCryptoWalletBySite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String cryptoWallet,
      @PathVariable String site) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsByCryptoWalletAndSite(cryptoWallet, site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/to/address/{address}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByToAddressAndSite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable String site) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsByToAddressAndSite(address, site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/from/address/{address}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByFromAddressAndSite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable String site) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsByFromAddressAndSite(address, site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/send/{send}/wallet/{cryptoWallet}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getSendDepositsByCryptoWalletAndSite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String cryptoWallet,
      @PathVariable String site,
      @PathVariable boolean send) {
    return ResponseEntity.ok()
        .body(
            cryptoService.getSendDepositsByCryptoWalletAndSite(cryptoWallet, site, send, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/send/{send}/to/address/{address}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByToAddressAndIsSendAndSite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable boolean send,
      @PathVariable String site,
      @PathVariable String address) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsByToAddressAndIsSendAndSite(address, send, site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/deposit/send/{send}/from/address/{address}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsByFromAddressAndIsSendAndSite(
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable boolean send,
      @PathVariable String site,
      @PathVariable String address) {
    return ResponseEntity.ok()
        .body(
            cryptoService.getDepositsByFromAddressAndIsSendAndSite(address, send, site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/account/range/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsInRangeBySite(
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String site) {
    return ResponseEntity.ok()
        .body(cryptoService.getDepositsInRangeBySite(start, end, site, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/account/range/address/to/{address}/send/{isSend}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsInRangeByToAddressAndIsSendAndSite(
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable boolean isSend,
      @PathVariable String site) {
    return ResponseEntity.ok()
        .body(
            cryptoService.getDepositsInRangeByToAddressAndIsSendAndSite(
                isSend, address, site, start, end, pageable));
  }

  @PreAuthorize(DEFAULT_ACCESS)
  @GetMapping("get/crypto/account/range/address/from/{address}/send/{isSend}/by/site/{site}")
  public ResponseEntity<Page<CryptoDepositDTO>> getDepositsInRangeByFromAddressAndIsSendAndSite(
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end,
      @PageableDefault(size = 10, sort = "requestedAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @PathVariable String address,
      @PathVariable boolean isSend,
      @PathVariable String site) {
    return ResponseEntity.ok()
        .body(
            cryptoService.getDepositsInRangeByFromAddressAndIsSendAndSite(
                isSend, address, site, start, end, pageable));
  }
}
