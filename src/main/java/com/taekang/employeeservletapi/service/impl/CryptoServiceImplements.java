package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.BalanceResponseDTO;
import com.taekang.employeeservletapi.DTO.UpdateIsSendDTO;
import com.taekang.employeeservletapi.DTO.UpdateMemoDTO;
import com.taekang.employeeservletapi.DTO.UpdateSiteDTO;
import com.taekang.employeeservletapi.DTO.crypto.*;
import com.taekang.employeeservletapi.api.CryptoBalanceAPI;
import com.taekang.employeeservletapi.entity.user.ChainType;
import com.taekang.employeeservletapi.entity.user.CryptoAccount;
import com.taekang.employeeservletapi.entity.user.CryptoDeposit;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import com.taekang.employeeservletapi.error.AccountNotFoundException;
import com.taekang.employeeservletapi.rabbitMQ.MessageProducer;
import com.taekang.employeeservletapi.repository.user.CryptoAccountRepository;
import com.taekang.employeeservletapi.repository.user.CryptoDepositRepository;
import com.taekang.employeeservletapi.service.CryptoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CryptoServiceImplements implements CryptoService {

  private final CryptoAccountRepository cryptoAccountRepository;

  private final CryptoDepositRepository cryptoDepositRepository;

  private final CryptoBalanceAPI cryptoBalanceAPI;

  private final MessageProducer messageProducer;
  private final ModelMapper modelMapper;

  @Autowired
  public CryptoServiceImplements(
      CryptoAccountRepository cryptoAccountRepository,
      CryptoDepositRepository cryptoDepositRepository,
      CryptoBalanceAPI cryptoBalanceAPI,
      MessageProducer messageProducer,
      ModelMapper modelMapper) {
    this.cryptoAccountRepository = cryptoAccountRepository;
    this.cryptoDepositRepository = cryptoDepositRepository;
    this.cryptoBalanceAPI = cryptoBalanceAPI;
    this.messageProducer = messageProducer;
    this.modelMapper = modelMapper;
  }

  private CryptoAccountDTO toCryptoAccountDTO(CryptoAccount cryptoAccount) {
    return modelMapper.map(cryptoAccount, CryptoAccountDTO.class);
  }

  private CryptoDepositDTO toCryptoDepositDTO(CryptoDeposit deposit) {
    return modelMapper.map(deposit, CryptoDepositDTO.class);
  }

  @Override
  public Page<CryptoAccountDTO> getCryptoAccountList(Pageable pageable) {
    return cryptoAccountRepository.findAll(pageable).map(this::toCryptoAccountDTO);
  }

  @Override
  public List<CryptoAccountDTO> getCryptoAccountByEmail(String email) {
    List<CryptoAccountDTO> result = new ArrayList<>();
    CryptoAccount cryptoAccount =
        cryptoAccountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    result.add(modelMapper.map(cryptoAccount, CryptoAccountDTO.class));

    return result;
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByCryptoWallet(String cryptoWallet, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_CryptoWallet(cryptoWallet, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByToAddressAndStatus(
      TransactionStatus status, String toAddress, Pageable pageable) {
    return cryptoDepositRepository
        .findByStatusAndToAddressOrderByRequestedAtDesc(status, toAddress, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByFromAddressAndStatus(
      TransactionStatus status, String fromAddress, Pageable pageable) {
    return cryptoDepositRepository
        .findByStatusAndFromAddressOrderByRequestedAtDesc(status, fromAddress, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsInRange(
      LocalDateTime start, LocalDateTime end, Pageable pageable) {
    return cryptoDepositRepository
        .findByRequestedAtBetweenOrderByRequestedAtDesc(start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsInRangeByToAddressAndIsSend(
      boolean isSend, String toAddress, LocalDateTime start, LocalDateTime end, Pageable pageable) {
    return cryptoDepositRepository
        .findByToAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
            toAddress, isSend, start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsInRangeByFromAddressAndIsSend(
      boolean isSend,
      String fromAddress,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    return cryptoDepositRepository
        .findByFromAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
            fromAddress, isSend, start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public void deleteDepositById(Long depositId) {
    cryptoDepositRepository.deleteById(depositId);
  }

  @Override
  public void updateSite(UpdateSiteDTO updateSiteDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findById(updateSiteDTO.getId())
            .orElseThrow(AccountNotFoundException::new);
    cryptoAccountRepository.save(cryptoAccount.toBuilder().site(updateSiteDTO.getSite()).build());
  }

  @Override
  public void updateMemo(UpdateMemoDTO updateMemoDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findById(updateMemoDTO.getId())
            .orElseThrow(AccountNotFoundException::new);
    cryptoAccountRepository.save(cryptoAccount.toBuilder().memo(updateMemoDTO.getMemo()).build());
  }

  @Override
  public Boolean updateSend(UpdateIsSendDTO updateIsSendDTO) {
    CryptoDeposit cryptoDeposit =
        cryptoDepositRepository
            .findById(updateIsSendDTO.getId())
            .orElseThrow(AccountNotFoundException::new);
    CryptoDeposit save =
        cryptoDepositRepository.save(
            cryptoDeposit.toBuilder()
                .id(updateIsSendDTO.getId())
                .isSend(updateIsSendDTO.isSend())
                .build());

    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(save.getFromAddress())
            .orElseThrow(AccountNotFoundException::new);

    DepositSentApprovalNotifyDTO message = DepositSentApprovalNotifyDTO.builder()
            .email(cryptoAccount.getEmail())
            .cryptoType(save.getCryptoType())
            .amount(save.getAmount())
            .requestAt(save.getRequestedAt())
            .build();

    messageProducer.sendDepositSendMessage(message);
    return save.isSend();
  }

  @Override
  public CryptoAccountDTO updateCryptoWallet(CryptoWalletUpdateDTO cryptoWalletUpdateDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(cryptoWalletUpdateDTO.getCryptoWallet())
            .orElseThrow(AccountNotFoundException::new);
    CryptoAccount save =
        cryptoAccountRepository.save(
            cryptoAccount.toBuilder()
                .cryptoWallet(cryptoWalletUpdateDTO.getCryptoWallet())
                .build());

    return modelMapper.map(save, CryptoAccountDTO.class);
  }

  @Override
  public CryptoAccountDTO getCryptoAccountByCryptoWallet(String cryptoWallet, Pageable pageable) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(cryptoWallet)
            .orElseThrow(AccountNotFoundException::new);
    return modelMapper.map(cryptoAccount, CryptoAccountDTO.class);
  }

  @Override
  public Page<CryptoAccountDTO> getCryptoAccountListBySite(String site, Pageable pageable) {
    return cryptoAccountRepository.findBySite(site, pageable).map(this::toCryptoAccountDTO);
  }

  @Override
  public List<CryptoAccountDTO> getCryptoAccountByEmailAndSite(String email, String site) {
    List<CryptoAccountDTO> result = new ArrayList<>();
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByEmailAndSite(email, site)
            .orElseThrow(AccountNotFoundException::new);
    result.add(modelMapper.map(cryptoAccount, CryptoAccountDTO.class));

    return result;
  }

  @Override
  public AccountSummaryInfoDTO getAccountSummaryInfoBySite(
      String site, String cryptoWallet, ChainType chainType) {
    Long depositHistoryLength = cryptoDepositRepository.countAllByCryptoAccount_Site(site);
    BalanceResponseDTO balance = cryptoBalanceAPI.getBalance(chainType, cryptoWallet);

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime beforeOneWeeks = now.minusWeeks(1);

    List<CryptoDeposit> deposits =
        cryptoDepositRepository.findByCryptoAccount_SiteAndRequestedAtBetween(
            site, beforeOneWeeks, beforeOneWeeks);

    BigDecimal weeksDepositAmount = BigDecimal.ZERO;
    BigDecimal todayDepositAmount = BigDecimal.ZERO;

    LocalDate today = now.toLocalDate();

    for (CryptoDeposit deposit : deposits) {
      BigDecimal amount = deposit.getKrwAmount();
      weeksDepositAmount = weeksDepositAmount.add(amount);

      // 날짜가 오늘과 같은 경우
      if (deposit.getRequestedAt().toLocalDate().isEqual(today)) {
        todayDepositAmount = todayDepositAmount.add(amount);
      }
    }

    return AccountSummaryInfoDTO.builder()
        .balance(balance.getBalance())
        .depositHistoryLength(depositHistoryLength)
        .todayDepositAmount(todayDepositAmount)
        .weeksDepositAmount(weeksDepositAmount)
        .build();
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsBySite(String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_Site(site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getSendDepositsBySite(
      String site, boolean send, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndIsSend(site, send, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByToAddressAndIsSendAndSite(
      String toAddress, boolean isSend, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndToAddressAndIsSend(site, toAddress, isSend, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByFromAddressAndIsSendAndSite(
      String fromAddress, boolean isSend, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndFromAddressAndIsSend(site, fromAddress, isSend, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByEmailAndSite(
      String email, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_EmailAndCryptoAccount_Site(email, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByCryptoWalletAndSite(
      String cryptoWallet, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_CryptoWalletAndCryptoAccount_Site(cryptoWallet, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByToAddressAndSite(
      String toAddress, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByToAddressAndCryptoAccount_Site(toAddress, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsByFromAddressAndSite(
      String fromAddress, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByFromAddressAndCryptoAccount_Site(fromAddress, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getSendDepositsByCryptoWalletAndSite(
      String cryptoWallet, String site, boolean isSend, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_CryptoWalletAndCryptoAccount_SiteAndIsSend(
            cryptoWallet, site, isSend, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsInRangeBySite(
      LocalDateTime start, LocalDateTime end, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndRequestedAtBetweenOrderByRequestedAtDesc(
            site, start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsInRangeByToAddressAndIsSendAndSite(
      boolean isSend,
      String toAddress,
      String site,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndToAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
            site, toAddress, isSend, start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  public Page<CryptoDepositDTO> getDepositsInRangeByFromAddressAndIsSendAndSite(
      boolean isSend,
      String fromAddress,
      String site,
      LocalDateTime start,
      LocalDateTime end,
      Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndFromAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
            site, fromAddress, isSend, start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }
}
