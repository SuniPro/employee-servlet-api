package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.NotifyDTO;
import com.taekang.employeeservletapi.DTO.NotifyWithReadDTO;
import com.taekang.employeeservletapi.entity.employee.Notify;
import com.taekang.employeeservletapi.service.NotifyService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/notify")
public class NotifyController {

  private static final String MANAGER_ACCESS =
      "hasAnyAuthority('LEVEL_DEVELOPER','LEVEL_ADMINISTRATOR','LEVEL_MANAGER')";

  private final NotifyService notifyService;
  private final JwtUtil jwtUtil;

  @Autowired
  public NotifyController(NotifyService notifyService, JwtUtil jwtUtil) {
    this.notifyService = notifyService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("create")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<Notify> createNotify(@RequestBody NotifyDTO notifyDTO) {
    return ResponseEntity.ok().body(notifyService.createNotify(notifyDTO));
  }

  @PutMapping("update")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<Notify> updateNotify(@RequestBody NotifyDTO notifyDTO) {
    return ResponseEntity.ok().body(notifyService.updateNotify(notifyDTO));
  }

  @GetMapping("get/all")
  public ResponseEntity<List<Notify>> getAllNotifyList() {
    return ResponseEntity.ok().body(notifyService.getAllNotifylist());
  }

  @GetMapping("get/by/id/{id}")
  public ResponseEntity<Notify> getNotifyById(@PathVariable Long id) {
    return ResponseEntity.ok().body(notifyService.getNotifyById(id));
  }

  @GetMapping("get/by/level")
  public ResponseEntity<List<Notify>> getNotifyByLevel(@CookieValue("access-token") String token) {
    return ResponseEntity.ok()
        .body(notifyService.getAllNotifyForLevel(jwtUtil.getEmployeeName(token)));
  }

  @GetMapping("get/latest")
  public ResponseEntity<Notify> getLatestNotify() {
    return ResponseEntity.ok().body(notifyService.getLatestNotify());
  }

  @GetMapping("get/all/with/read")
  public ResponseEntity<List<NotifyWithReadDTO>> getAllNotifyWithReadState(
      @CookieValue("access-token") String token) {
    return ResponseEntity.ok()
        .body(notifyService.getAllNotifyWithReadState(jwtUtil.getEmployeeName(token)));
  }

  @DeleteMapping("delete/{id}")
  @PreAuthorize(MANAGER_ACCESS)
  public void deleteNotify(@PathVariable Long id) {
    notifyService.deleteNotify(id);
  }

  @PatchMapping("read/{id}")
  public void read(@CookieValue("access-token") String token, @PathVariable Long id) {
    notifyService.markAsRead(id, jwtUtil.getEmployeeName(token));
  }

  @GetMapping("read/about/notify/{id}/{name}")
  public ResponseEntity<Boolean> isRead(@PathVariable Long id, @PathVariable String name) {
    return ResponseEntity.ok().body(notifyService.isNotifyRead(id, name));
  }

  @GetMapping("count/notify")
  public ResponseEntity<Long> getUnreadNotifyCount(@CookieValue("access-token") String token) {
    return ResponseEntity.ok()
        .body(notifyService.countUnreadNotify(jwtUtil.getEmployeeName(token)));
  }

  @GetMapping("get/read")
  public ResponseEntity<List<Notify>> getNotifyListByReadEmployee(
      @CookieValue("access-token") String token) {
    return ResponseEntity.ok()
        .body(notifyService.getReadNotifyListByEmployee(jwtUtil.getEmployeeName(token)));
  }

  @GetMapping("get/unread/list")
  public ResponseEntity<List<Notify>> getNotifyListByUnReadEmployee(
      @CookieValue("access-token") String token) {
    return ResponseEntity.ok()
        .body(notifyService.getUnreadNotifyListByEmployee(jwtUtil.getEmployeeName(token)));
  }

  @GetMapping("get/unread/all")
  public ResponseEntity<List<Notify>> getAllNotifyListByUnReadEmployee(
      @CookieValue("access-token") String token) {
    return ResponseEntity.ok()
        .body(notifyService.getAllUnreadNotifyListByEmployee(jwtUtil.getEmployeeName(token)));
  }
}
