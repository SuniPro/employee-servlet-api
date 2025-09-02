package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.DTO.crypto.UpdateCryptoWalletDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SiteService {

  List<SiteDTO> getAllSite();

  Page<SiteDTO> getAllThoughPage(Pageable pageable);

  SiteDTO getBySite(String site);

  SiteDTO createSite(CreateSiteDTO createSiteDTO, String name);

  SiteOnlyDTO updateOnlySite(UpdateSiteDTO updateSiteDTO, String name);
  
  SiteWalletDTO updateSiteWallet(UpdateCryptoWalletDTO updateCryptoWalletDTO, String name);

  List<SiteWalletInfoDTO> getSiteWalletInfoBySite(String site);

  SiteDTO deleteSite(Long siteId, String name);
}
