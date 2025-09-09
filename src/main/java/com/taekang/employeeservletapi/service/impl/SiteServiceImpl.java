package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.DTO.crypto.UpdateCryptoWalletDTO;
import com.taekang.employeeservletapi.api.CryptoBalanceAPI;
import com.taekang.employeeservletapi.entity.employee.Site;
import com.taekang.employeeservletapi.entity.employee.SiteWallet;
import com.taekang.employeeservletapi.entity.user.CryptoDeposit;
import com.taekang.employeeservletapi.error.CannotFoundSiteException;
import com.taekang.employeeservletapi.error.CannotFoundWalletException;
import com.taekang.employeeservletapi.repository.employee.SiteRepository;
import com.taekang.employeeservletapi.repository.employee.SiteWalletRepository;
import com.taekang.employeeservletapi.repository.user.CryptoAccountRepository;
import com.taekang.employeeservletapi.repository.user.CryptoDepositRepository;
import com.taekang.employeeservletapi.service.SiteService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SiteServiceImpl implements SiteService {

  private final SiteRepository siteRepository;
  private final SiteWalletRepository siteWalletRepository;
  private final CryptoAccountRepository cryptoAccountRepository;
  private final CryptoDepositRepository cryptoDepositRepository;
  private final CryptoBalanceAPI cryptoBalanceAPI;
  private final ModelMapper modelMapper;

  @Autowired
  public SiteServiceImpl(
      SiteRepository siteRepository,
      SiteWalletRepository siteWalletRepository,
      CryptoAccountRepository cryptoAccountRepository,
      CryptoDepositRepository cryptoDepositRepository,
      CryptoBalanceAPI cryptoBalanceAPI,
      ModelMapper modelMapper) {
    this.siteRepository = siteRepository;
    this.siteWalletRepository = siteWalletRepository;
    this.cryptoAccountRepository = cryptoAccountRepository;
    this.cryptoDepositRepository = cryptoDepositRepository;
    this.cryptoBalanceAPI = cryptoBalanceAPI;
    this.modelMapper = modelMapper;
  }

  public SiteDTO toSiteDTO(Site site) {
    return modelMapper.map(site, SiteDTO.class);
  }

  @Override
  public List<SiteDTO> getAllSite() {
    List<Site> siteList = siteRepository.findByDeleteDateTimeIsNull();

    if (siteList.isEmpty()) return List.of();

    List<Long> siteIdList = siteList.stream().map(Site::getId).toList();
    List<SiteWallet> wallets = siteWalletRepository.findBySite_IdIn(siteIdList);

    Map<Long, List<SiteWallet>> bySiteId =
        wallets.stream().collect(Collectors.groupingBy(w -> w.getSite().getId()));

    return siteList.stream()
        .map(
            site -> {
              List<SiteWalletDTO> walletDTOs =
                  bySiteId.getOrDefault(site.getId(), List.of()).stream()
                      .map(w -> modelMapper.map(w, SiteWalletDTO.class))
                      .toList();

              return SiteDTO.builder()
                  .id(site.getId())
                  .site(site.getSite())
                  .siteWalletList(walletDTOs)
                  .telegramChatId(site.getTelegramChatId())
                  .telegramUsername(site.getTelegramUsername())
                  .insertDateTime(site.getInsertDateTime())
                  .insertId(site.getInsertId())
                  .updateDateTime(site.getUpdateDateTime())
                  .updateId(site.getUpdateId())
                  .build();
            })
        .toList();
  }

  @Override
  public Page<SiteDTO> getAllThoughPage(Pageable pageable) {
    return siteRepository.findByDeleteDateTimeIsNull(pageable).map(this::toSiteDTO);
  }

  @Override
  public SiteDTO getBySite(String site) {
    Site siteObject = siteRepository.findBySite(site).orElseThrow();
    List<SiteWalletDTO> list =
        siteWalletRepository.findBySite_Id(siteObject.getId()).stream()
            .map(wallet -> modelMapper.map(wallet, SiteWalletDTO.class))
            .toList();

    return SiteDTO.builder()
        .id(siteObject.getId())
        .site(site)
        .siteWalletList(list)
        .telegramChatId(siteObject.getTelegramChatId())
        .telegramUsername(siteObject.getTelegramUsername())
        .insertDateTime(siteObject.getInsertDateTime())
        .insertId(siteObject.getInsertId())
        .updateDateTime(siteObject.getUpdateDateTime())
        .updateId(siteObject.getUpdateId())
        .build();
  }

  @Transactional
  @Override
  public SiteDTO createSite(CreateSiteDTO createSiteDTO, String name) {
    Site siteEntity = Site.builder().site(createSiteDTO.getSite()).insertId(name).build();
    Site save = siteRepository.save(siteEntity);

    createSiteDTO
        .getSiteWalletList()
        .forEach(
            wallet -> {
              SiteWallet siteWallet =
                  SiteWallet.builder()
                      .site(save)
                      .cryptoWallet(wallet.getCryptoWallet())
                      .chainType(wallet.getChainType())
                      .insertId(name)
                      .build();

              siteWalletRepository.save(siteWallet);
            });

    return modelMapper.map(siteRepository.save(siteEntity), SiteDTO.class);
  }

  @Transactional
  @Override
  public SiteOnlyDTO updateOnlySite(UpdateSiteDTO updateSiteDTO, String name) {
    // 1) 기존 Site 조회(merge 위험 제거)
    Site current =
        siteRepository.findById(updateSiteDTO.getId()).orElseThrow(CannotFoundSiteException::new);

    Site updated = current.toBuilder().site(updateSiteDTO.getSite()).updateId(name).build();

    siteRepository.save(updated); // merge 1회

    siteRepository.updateSiteName(updateSiteDTO.getId(), updateSiteDTO.getSite(), name);
    cryptoAccountRepository.bulkUpdateSite(current.getSite(), updateSiteDTO.getSite());

    return modelMapper.map(updated, SiteOnlyDTO.class);
  }

  @Transactional
  @Override
  public SiteWalletDTO updateSiteWallet(UpdateCryptoWalletDTO updateCryptoWalletDTO, String name) {
    SiteWallet byId =
        siteWalletRepository
            .findById(updateCryptoWalletDTO.getId())
            .orElseThrow(CannotFoundWalletException::new);

    SiteWallet build =
        byId.toBuilder()
            .cryptoWallet(updateCryptoWalletDTO.getCryptoWallet())
            .chainType(updateCryptoWalletDTO.getChainType())
            .updateId(name)
            .build();

    return modelMapper.map(siteWalletRepository.save(build), SiteWalletDTO.class);
  }

  @Transactional
  @Override
  public SiteDTO deleteSite(Long siteId, String name) {
    ZonedDateTime koreaTimeNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    Site siteEntity =
        Site.builder()
            .id(siteId)
            .deleteDateTime(koreaTimeNow.toLocalDateTime())
            .deleteId(name)
            .build();

    return modelMapper.map(siteRepository.save(siteEntity), SiteDTO.class);
  }

  @Override
  public List<SiteWalletInfoDTO> getSiteWalletInfoBySite(String site) {
    List<SiteWalletInfoDTO> result = new ArrayList<>();
    Site siteEntity = siteRepository.findBySite(site).orElseThrow(CannotFoundSiteException::new);

    Long depositHistoryLength = cryptoDepositRepository.countAllByCryptoAccount_Site(site);

    List<SiteWallet> siteWalletList = siteWalletRepository.findBySite_Id(siteEntity.getId());

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime beforeOneWeeks = now.minusWeeks(1);

    siteWalletList.forEach(
        wallet -> {
          BalanceResponseDTO balance =
              cryptoBalanceAPI.getBalance(wallet.getChainType(), wallet.getCryptoWallet());

          List<CryptoDeposit> deposits =
              cryptoDepositRepository.findByToAddressAndRequestedAtBetween(
                      wallet.getCryptoWallet(), beforeOneWeeks, now);

          BigDecimal weeksDepositAmount = BigDecimal.ZERO;
          BigDecimal todayDepositAmount = BigDecimal.ZERO;

          LocalDate today = now.toLocalDate();

          for (CryptoDeposit deposit : deposits) {
            BigDecimal amount = deposit.getAmount();
            weeksDepositAmount = weeksDepositAmount.add(amount);

            // 날짜가 오늘과 같은 경우
            if (deposit.getRequestedAt().toLocalDate().isEqual(today)) {
              todayDepositAmount = todayDepositAmount.add(amount);
            }
          }

          SiteWalletInfoDTO siteWalletInfoDTO =
              SiteWalletInfoDTO.builder()
                  .id(wallet.getId())
                  .cryptoWallet(wallet.getCryptoWallet())
                  .chainType(wallet.getChainType())
                  .balance(balance.getBalance())
                  .depositHistoryLength(depositHistoryLength)
                  .todayDepositAmount(todayDepositAmount)
                  .weeksDepositAmount(weeksDepositAmount)
                  .insertDateTime(wallet.getInsertDateTime())
                  .updateDateTime(wallet.getUpdateDateTime())
                  .build();

          result.add(siteWalletInfoDTO);
        });

    return result;
  }
}
