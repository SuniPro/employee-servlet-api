package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.DTO.crypto.*;
import com.taekang.employeeservletapi.entity.user.CryptoAccount;
import com.taekang.employeeservletapi.entity.user.CryptoDeposit;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import com.taekang.employeeservletapi.error.AccountNotFoundException;
import com.taekang.employeeservletapi.error.CannotFoundWalletException;
import com.taekang.employeeservletapi.rabbitMQ.MessageProducer;
import com.taekang.employeeservletapi.repository.user.CryptoAccountRepository;
import com.taekang.employeeservletapi.repository.user.CryptoDepositRepository;
import com.taekang.employeeservletapi.service.CryptoService;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CryptoServiceImplements implements CryptoService {

  private final CryptoAccountRepository cryptoAccountRepository;

  private final CryptoDepositRepository cryptoDepositRepository;

  private final MessageProducer messageProducer;
  private final ModelMapper modelMapper;

  @Autowired
  public CryptoServiceImplements(
      CryptoAccountRepository cryptoAccountRepository,
      CryptoDepositRepository cryptoDepositRepository,
      MessageProducer messageProducer,
      ModelMapper modelMapper) {
    this.cryptoAccountRepository = cryptoAccountRepository;
    this.cryptoDepositRepository = cryptoDepositRepository;
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
  @Transactional(readOnly = true)
  public Page<CryptoAccountDTO> getCryptoAccountList(Pageable pageable) {
    return cryptoAccountRepository.findAll(pageable).map(this::toCryptoAccountDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CryptoAccountDTO> getCryptoAccountByEmail(String email) {
    List<CryptoAccountDTO> result = new ArrayList<>();
    CryptoAccount cryptoAccount =
        cryptoAccountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    result.add(modelMapper.map(cryptoAccount, CryptoAccountDTO.class));

    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByCryptoWallet(String cryptoWallet, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_CryptoWallet(cryptoWallet, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByToAddressAndStatus(
      TransactionStatus status, String toAddress, Pageable pageable) {
    return cryptoDepositRepository
        .findByStatusAndToAddressOrderByRequestedAtDesc(status, toAddress, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByFromAddressAndStatus(
      TransactionStatus status, String fromAddress, Pageable pageable) {
    return cryptoDepositRepository
        .findByStatusAndFromAddressOrderByRequestedAtDesc(status, fromAddress, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsInRange(
      LocalDateTime start, LocalDateTime end, Pageable pageable) {
    return cryptoDepositRepository
        .findByRequestedAtBetweenOrderByRequestedAtDesc(start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsInRangeByToAddressAndIsSend(
      boolean isSend, String toAddress, LocalDateTime start, LocalDateTime end, Pageable pageable) {
    return cryptoDepositRepository
        .findByToAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
            toAddress, isSend, start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
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
  @Transactional
  public void deleteDepositById(Long depositId) {
    cryptoDepositRepository.deleteById(depositId);
  }

  @Override
  @Transactional
  public void updateSite(UpdateSiteDTO updateSiteDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findById(updateSiteDTO.getId())
            .orElseThrow(AccountNotFoundException::new);
    cryptoAccountRepository.save(cryptoAccount.toBuilder().site(updateSiteDTO.getSite()).build());
  }

  @Override
  @Transactional
  public void updateMemo(UpdateMemoDTO updateMemoDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findById(updateMemoDTO.getId())
            .orElseThrow(AccountNotFoundException::new);
    cryptoAccountRepository.save(cryptoAccount.toBuilder().memo(updateMemoDTO.getMemo()).build());
  }

  @Override
  @Transactional
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

    DepositSentApprovalNotifyDTO message =
        DepositSentApprovalNotifyDTO.builder()
            .email(cryptoAccount.getEmail())
            .cryptoType(save.getCryptoType())
            .amount(save.getAmount())
            .realAmount(save.getRealAmount())
            .requestAt(save.getRequestedAt())
            .build();

    messageProducer.sendDepositSendMessage(message);
    return save.isSend();
  }

  @Override
  @Transactional
  public CryptoAccountDTO updateCryptoWallet(UpdateCryptoWalletDTO updateCryptoWalletDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findById(updateCryptoWalletDTO.getId())
            .orElseThrow(AccountNotFoundException::new);
    CryptoAccount save =
        cryptoAccountRepository.save(
            cryptoAccount.toBuilder()
                .cryptoWallet(updateCryptoWalletDTO.getCryptoWallet())
                .build());

    return modelMapper.map(save, CryptoAccountDTO.class);
  }

  @Override
  @Transactional(readOnly = true)
  public CryptoAccountDTO getCryptoAccountByCryptoWallet(String cryptoWallet) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(cryptoWallet)
            .orElseThrow(AccountNotFoundException::new);
    return modelMapper.map(cryptoAccount, CryptoAccountDTO.class);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoAccountDTO> getCryptoAccountListBySite(String site, Pageable pageable) {
    return cryptoAccountRepository.findBySite(site, pageable).map(this::toCryptoAccountDTO);
  }

  @Override
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsBySite(String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_Site(site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getSendDepositsBySite(
      String site, boolean send, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndIsSend(site, send, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByToAddressAndIsSendAndSite(
      String toAddress, boolean isSend, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndToAddressAndIsSend(site, toAddress, isSend, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByFromAddressAndIsSendAndSite(
      String fromAddress, boolean isSend, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndFromAddressAndIsSend(site, fromAddress, isSend, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByEmailAndSite(
      String email, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_EmailAndCryptoAccount_Site(email, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional
  public CryptoDepositDTO createSentDeposit(CryptoDepositDTO cryptoDepositDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(cryptoDepositDTO.getFromAddress())
            .orElseThrow(CannotFoundWalletException::new);

    CryptoDeposit build =
        CryptoDeposit.builder()
            .status(TransactionStatus.CONFIRMED)
            .cryptoAccount(cryptoAccount)
            .chainType(cryptoDepositDTO.getChainType())
            .cryptoType(cryptoDepositDTO.getCryptoType())
            .fromAddress(cryptoDepositDTO.getFromAddress())
            .toAddress(cryptoDepositDTO.getToAddress())
            .amount(cryptoDepositDTO.getAmount())
            .krwAmount(cryptoDepositDTO.getKrwAmount())
            .realAmount(cryptoDepositDTO.getRealAmount())
            .accepted(true)
            .acceptedAt(LocalDateTime.now())
            .requestedAt(LocalDateTime.now())
            .isSend(true)
            .build();

    CryptoDeposit save = cryptoDepositRepository.save(build);

    return modelMapper.map(save, CryptoDepositDTO.class);
  }

  @Override
  @Transactional
  public CryptoDepositDTO createNotSentDeposit(CryptoDepositDTO cryptoDepositDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(cryptoDepositDTO.getFromAddress())
            .orElseThrow(CannotFoundWalletException::new);

    CryptoDeposit build =
        CryptoDeposit.builder()
            .status(TransactionStatus.PENDING)
            .cryptoAccount(cryptoAccount)
            .chainType(cryptoDepositDTO.getChainType())
            .cryptoType(cryptoDepositDTO.getCryptoType())
            .fromAddress(cryptoDepositDTO.getFromAddress())
            .toAddress(cryptoDepositDTO.getToAddress())
            .amount(cryptoDepositDTO.getAmount())
            .krwAmount(cryptoDepositDTO.getKrwAmount())
            .realAmount(cryptoDepositDTO.getRealAmount())
            .accepted(true)
            .acceptedAt(LocalDateTime.now())
            .requestedAt(LocalDateTime.now())
            .isSend(false)
            .build();

    CryptoDeposit save = cryptoDepositRepository.save(build);

    return modelMapper.map(save, CryptoDepositDTO.class);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByCryptoWalletAndSite(
      String cryptoWallet, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_CryptoWalletAndCryptoAccount_Site(cryptoWallet, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByToAddressAndSite(
      String toAddress, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByToAddressAndCryptoAccount_Site(toAddress, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsByFromAddressAndSite(
      String fromAddress, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByFromAddressAndCryptoAccount_Site(fromAddress, site, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getSendDepositsByCryptoWalletAndSite(
      String cryptoWallet, String site, boolean isSend, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_CryptoWalletAndCryptoAccount_SiteAndIsSend(
            cryptoWallet, site, isSend, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CryptoDepositDTO> getDepositsInRangeBySite(
      LocalDateTime start, LocalDateTime end, String site, Pageable pageable) {
    return cryptoDepositRepository
        .findByCryptoAccount_SiteAndRequestedAtBetweenOrderByRequestedAtDesc(
            site, start, end, pageable)
        .map(this::toCryptoDepositDTO);
  }

  @Override
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
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
