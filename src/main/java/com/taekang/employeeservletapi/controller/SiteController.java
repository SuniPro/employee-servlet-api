package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.CreateSiteDTO;
import com.taekang.employeeservletapi.DTO.SiteDTO;
import com.taekang.employeeservletapi.error.TokenNotFoundedException;
import com.taekang.employeeservletapi.error.TokenNotValidateException;
import com.taekang.employeeservletapi.service.SiteService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  @GetMapping("get/{site}")
  public ResponseEntity<SiteDTO> getSite(@PathVariable String site) {
    return ResponseEntity.ok().body(siteService.getBySite(site));
  }

  @PostMapping("create")
  public ResponseEntity<SiteDTO> createSite(HttpServletRequest request, @RequestBody CreateSiteDTO createSiteDTO) {

    String token = jwtUtil.getAccessTokenInCookie(request);

    if (token == null) {
      throw new TokenNotFoundedException();
    }

    boolean validationToken = jwtUtil.validateToken(token);
    if (!validationToken) {
      throw new TokenNotValidateException();
    }

    return ResponseEntity.ok().body(siteService.createSite(createSiteDTO, jwtUtil.getEmployeeName(token)));
  }

  @PostMapping("update")
  public ResponseEntity<SiteDTO> updateSite(HttpServletRequest request, @RequestBody SiteDTO siteDTO) {

    String token = jwtUtil.getAccessTokenInCookie(request);

    if (token == null) {
      throw new TokenNotFoundedException();
    }

    boolean validationToken = jwtUtil.validateToken(token);
    if (!validationToken) {
      throw new TokenNotValidateException();
    }

    return ResponseEntity.ok().body(siteService.updateSite(siteDTO, jwtUtil.getEmployeeName(token)));
  }

  @PostMapping("delete")
  public ResponseEntity<SiteDTO> updateSite(HttpServletRequest request, @RequestBody Long siteId) {

    String token = jwtUtil.getAccessTokenInCookie(request);

    if (token == null) {
      throw new TokenNotFoundedException();
    }

    boolean validationToken = jwtUtil.validateToken(token);
    if (!validationToken) {
      throw new TokenNotValidateException();
    }

    return ResponseEntity.ok().body(siteService.deleteSite(siteId, jwtUtil.getEmployeeName(token)));
  }
}
