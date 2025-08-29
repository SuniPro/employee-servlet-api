package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.CreateSiteDTO;
import com.taekang.employeeservletapi.DTO.SiteDTO;

import java.util.List;

public interface SiteService {

  List<SiteDTO> getAllSite();

  SiteDTO getBySite(String site);

  SiteDTO createSite(CreateSiteDTO createSiteDTO, String name);

  SiteDTO updateSite(SiteDTO siteDTO, String name);

  SiteDTO deleteSite(Long siteId, String name);
}
