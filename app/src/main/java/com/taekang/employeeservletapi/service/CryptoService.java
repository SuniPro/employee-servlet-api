package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.DTO.crypto.CryptoAccountDTO;
import com.taekang.employeeservletapi.DTO.crypto.CryptoDepositDTO;
import com.taekang.employeeservletapi.DTO.crypto.UpdateCryptoWalletDTO;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CryptoService {

  /* 관리자 전용 : Account */
  Page<CryptoAccountDTO> getCryptoAccountList(Pageable pageable);

  List<CryptoAccountDTO> getCryptoAccountByEmail(String email);

  /* 관리자 전용 : Deposit */
  Page<CryptoDepositDTO> getDepositsByCryptoWallet(String cryptoWallet, Pageable pageable);

  // To Address & State 기준으로 정렬
  Page<CryptoDepositDTO> getDepositsByToAddressAndStatus(
      TransactionStatus status, String toAddress, Pageable pageable);

  // From Address & State 기준으로 정렬
  Page<CryptoDepositDTO> getDepositsByFromAddressAndStatus(
          TransactionStatus status, String fromAddress, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsInRange(LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsInRangeByToAddressAndIsSend(
          boolean isSend,
          String toAddress,
          LocalDateTime start,
          LocalDateTime end,
          Pageable pageable);

  Page<CryptoDepositDTO> getDepositsInRangeByFromAddressAndIsSend(
          boolean isSend,
          String fromAddress,
          LocalDateTime start,
          LocalDateTime end,
          Pageable pageable);

  void deleteDepositById(Long depositId);

  /* Account 상태 변경 */
  void updateSite(UpdateSiteDTO updateSiteDTO);

  void updateMemo(UpdateMemoDTO updateMemoDTO);

  /* Deposit 상태 변경 */
  Boolean updateSend(UpdateIsSendDTO updateIsSendDTO);

  CryptoAccountDTO updateCryptoWallet(UpdateCryptoWalletDTO updateCryptoWalletDTO);
  
  /* 유저 영역 */

  /* Account */
  Page<CryptoAccountDTO> getCryptoAccountListBySite(String site, Pageable pageable);
  List<CryptoAccountDTO> getCryptoAccountByEmailAndSite(String email, String site);
  
  CryptoAccountDTO getCryptoAccountByCryptoWallet(String cryptoWallet, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsBySite(String site, Pageable pageable);

  Page<CryptoDepositDTO> getSendDepositsBySite(String site, boolean send, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsByEmailAndSite(String email, String site, Pageable pageable);

  /* 특정 지갑의 입금내역 조회 */
  Page<CryptoDepositDTO> getDepositsByCryptoWalletAndSite(String cryptoWallet, String site, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsByToAddressAndSite(String toAddress, String site, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsByFromAddressAndSite(String fromAddress, String site, Pageable pageable);

  Page<CryptoDepositDTO> getSendDepositsByCryptoWalletAndSite(String cryptoWallet, String site, boolean isSend, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsByToAddressAndIsSendAndSite(String toAddress, boolean isSend, String site, Pageable pageable);

  Page<CryptoDepositDTO> getDepositsByFromAddressAndIsSendAndSite(String fromAddress, boolean isSend, String site, Pageable pageable);

  // 특정 기간의 전체 입금 목록
  Page<CryptoDepositDTO> getDepositsInRangeBySite(LocalDateTime start, LocalDateTime end, String site, Pageable pageable);
  
  // 특정 지갑의 기간 내 To Address의 입금 목록 & 보낸 내역 추가검증
  Page<CryptoDepositDTO> getDepositsInRangeByToAddressAndIsSendAndSite(
          boolean isSend,
          String toAddress,
          String site,
          LocalDateTime start,
          LocalDateTime end,
          Pageable pageable);

  // 특정 지갑의 기간 내 From Address의 입금 목록 & 보낸 내역 추가검증
  Page<CryptoDepositDTO> getDepositsInRangeByFromAddressAndIsSendAndSite(
          boolean isSend,
          String fromAddress,
          String site,
          LocalDateTime start,
          LocalDateTime end,
          Pageable pageable);
}
