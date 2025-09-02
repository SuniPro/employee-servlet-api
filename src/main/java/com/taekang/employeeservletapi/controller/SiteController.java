package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.DTO.crypto.UpdateCryptoWalletDTO;
import com.taekang.employeeservletapi.service.SiteService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("site")
public class SiteController {

  private final SiteService siteService;
  private final JwtUtil jwtUtil;

  @Autowired
  public SiteController(SiteService siteService, JwtUtil jwtUtil) {
    this.siteService = siteService;
    this.jwtUtil = jwtUtil;
  }

  @GetMapping("get/all")
  public ResponseEntity<List<SiteDTO>> getAll() {
    return ResponseEntity.ok().body(siteService.getAllSite());
  }

  @GetMapping("get/all/through/page")
  public ResponseEntity<Page<SiteDTO>> getAllThroughPage(
      @PageableDefault(size = 10, sort = "insertDateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok().body(siteService.getAllThoughPage(pageable));
  }

  @GetMapping("get/{site}")
  public ResponseEntity<SiteDTO> getSite(@PathVariable String site) {
    return ResponseEntity.ok().body(siteService.getBySite(site));
  }

  @PostMapping("create")
  public ResponseEntity<SiteDTO> createSite(
      @CookieValue("access-token") String token, @RequestBody CreateSiteDTO createSiteDTO) {
    String name = jwtUtil.getEmployeeName(token);

    return ResponseEntity.ok().body(siteService.createSite(createSiteDTO, name));
  }

  @PatchMapping("update/only/site")
  public ResponseEntity<SiteOnlyDTO> updateOnlySite(
      @CookieValue("access-token") String token, @RequestBody UpdateSiteDTO updateSiteDTO) {

    return ResponseEntity.ok()
        .body(siteService.updateOnlySite(updateSiteDTO, jwtUtil.getEmployeeName(token)));
  }

  @PatchMapping("update/only/wallet")
  public ResponseEntity<SiteWalletDTO> updateSiteWallet(
      @CookieValue("access-token") String token,
      @RequestBody UpdateCryptoWalletDTO updateCryptoWalletDTO) {

    return ResponseEntity.ok()
        .body(siteService.updateSiteWallet(updateCryptoWalletDTO, jwtUtil.getEmployeeName(token)));
  }

  @PostMapping("delete")
  public ResponseEntity<SiteDTO> updateSite(
      @CookieValue("access-token") String token, @RequestBody Long siteId) {
    return ResponseEntity.ok().body(siteService.deleteSite(siteId, jwtUtil.getEmployeeName(token)));
  }

  @GetMapping("get/wallet/info")
  public ResponseEntity<List<SiteWalletInfoDTO>> getSiteWalletInfoBySite(
      @CookieValue("access-token") String token) {
    return ResponseEntity.ok().body(siteService.getSiteWalletInfoBySite(jwtUtil.getSite(token)));
  }
}
