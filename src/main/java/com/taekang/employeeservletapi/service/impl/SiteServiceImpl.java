package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.CreateSiteDTO;
import com.taekang.employeeservletapi.DTO.SiteDTO;
import com.taekang.employeeservletapi.entity.employee.Site;
import com.taekang.employeeservletapi.repository.employee.SiteRepository;
import com.taekang.employeeservletapi.service.SiteService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteServiceImpl implements SiteService {

  private final SiteRepository siteRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public SiteServiceImpl(SiteRepository siteRepository, ModelMapper modelMapper) {
    this.siteRepository = siteRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<SiteDTO> getAllSite() {
    return siteRepository.findByDeleteDateTimeIsNull().stream()
        .map(site -> modelMapper.map(site, SiteDTO.class))
        .collect(Collectors.toList());
  }

  @Override
  public SiteDTO getBySite(String site) {
    Site siteObject = siteRepository.findBySite(site).orElseThrow();
    return modelMapper.map(siteObject, SiteDTO.class);
  }

  @Override
  public SiteDTO createSite(CreateSiteDTO createSiteDTO, String name) {
    Site siteEntity = Site.builder().site(createSiteDTO.getSite()).cryptoWallet(createSiteDTO.getCryptoWallet()).chainType(createSiteDTO.getChainType()).insertId(name).build();

    return modelMapper.map(siteRepository.save(siteEntity), SiteDTO.class);
  }

  @Override
  public SiteDTO updateSite(SiteDTO siteDTO, String name) {
    Site siteEntity =
        Site.builder().id(siteDTO.getId()).site(siteDTO.getSite()).updateId(name).build();

    return modelMapper.map(siteRepository.save(siteEntity), SiteDTO.class);
  }

  @Override
  public SiteDTO deleteSite(Long siteId, String name) {
    ZonedDateTime koreaTimeNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    Site siteEntity =
            Site.builder().id(siteId)
                    .deleteDateTime(koreaTimeNow.toLocalDateTime())
                    .deleteId(name).build();

    return modelMapper.map(siteRepository.save(siteEntity), SiteDTO.class);
  }
  
}
