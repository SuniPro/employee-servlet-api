package com.taekang.employeeservletapi.repository.user;

import com.taekang.employeeservletapi.entity.user.CryptoDeposit;
import com.taekang.employeeservletapi.entity.user.TransactionStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoDepositRepository extends JpaRepository<CryptoDeposit, Long> {

  Optional<CryptoDeposit> findById(Long id);

  Page<CryptoDeposit> findByCryptoAccount_CryptoWallet(
      String cryptoAccountCryptoWallet, Pageable pageable);

  Page<CryptoDeposit> findByStatusAndToAddressOrderByRequestedAtDesc(
      TransactionStatus status, String toAddress, Pageable pageable);

  Page<CryptoDeposit> findByStatusAndFromAddressOrderByRequestedAtDesc(
      TransactionStatus status, String toAddress, Pageable pageable);

  Page<CryptoDeposit> findByRequestedAtBetweenOrderByRequestedAtDesc(
      LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<CryptoDeposit> findByToAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
      String toAddress, boolean send, LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<CryptoDeposit> findByFromAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
      String fromAddress, boolean send, LocalDateTime start, LocalDateTime end, Pageable pageable);

  Long countAllByCryptoAccount_Site(String site);

  List<CryptoDeposit> findByToAddressAndRequestedAtBetween(
      String fromAddress, LocalDateTime requestedAtAfter, LocalDateTime requestedAtBefore);

  Page<CryptoDeposit> findByCryptoAccount_Site(String cryptoAccountSite, Pageable pageable);

  Page<CryptoDeposit> findByCryptoAccount_SiteAndIsSend(
      String cryptoAccountSite, boolean send, Pageable pageable);

  Page<CryptoDeposit> findByCryptoAccount_SiteAndToAddressAndIsSend(
      String site, String toAddress, boolean send, Pageable pageable);

  Page<CryptoDeposit> findByCryptoAccount_SiteAndFromAddressAndIsSend(
      String site, String toAddress, boolean send, Pageable pageable);

  Page<CryptoDeposit> findByCryptoAccount_EmailAndCryptoAccount_Site(
      String email, String site, Pageable pageable);

  Page<CryptoDeposit> findByCryptoAccount_CryptoWalletAndCryptoAccount_Site(
      String cryptoWallet, String site, Pageable pageable);

  Page<CryptoDeposit> findByToAddressAndCryptoAccount_Site(
      String toAddress, String site, Pageable pageable);

  Page<CryptoDeposit> findByFromAddressAndCryptoAccount_Site(
      String from, String site, Pageable pageable);

  Page<CryptoDeposit> findByCryptoAccount_CryptoWalletAndCryptoAccount_SiteAndIsSend(
      String cryptoWallet, String site, boolean send, Pageable pageable);

  Page<CryptoDeposit> findByCryptoAccount_SiteAndRequestedAtBetweenOrderByRequestedAtDesc(
      String site, LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<CryptoDeposit>
      findByCryptoAccount_SiteAndToAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
          String site,
          String toAddress,
          boolean send,
          LocalDateTime start,
          LocalDateTime end,
          Pageable pageable);

  Page<CryptoDeposit>
      findByCryptoAccount_SiteAndFromAddressAndIsSendAndRequestedAtBetweenOrderByRequestedAtDesc(
          String site,
          String fromAddress,
          boolean send,
          LocalDateTime start,
          LocalDateTime end,
          Pageable pageable);
}
