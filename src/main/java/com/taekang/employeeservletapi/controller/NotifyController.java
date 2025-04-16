package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.NotifyDTO;
import com.taekang.employeeservletapi.DTO.NotifyWithReadDTO;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Notify;
import com.taekang.employeeservletapi.service.NotifyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notify")
public class NotifyController {

  private static final String MANAGER_ACCESS =
          "hasAnyAuthority('LEVEL_CEO', 'LEVEL_COO', 'LEVEL_CFO', 'LEVEL_CIO', 'LEVEL_CTO', 'LEVEL_CDO', 'LEVEL_MANAGER', 'LEVEL_OFFICEMANAGER', 'LEVEL_SENIORMANAGER')";


  private final NotifyService notifyService;

  @Autowired
  public NotifyController(NotifyService notifyService) {
    this.notifyService = notifyService;
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

  @GetMapping("get/by/level/{level}")
  public ResponseEntity<List<Notify>> getNotifyById(@PathVariable Level level) {
    return ResponseEntity.ok().body(notifyService.getAllNotifyForLevel(level));
  }

  @GetMapping("get/latest")
  public ResponseEntity<Notify> getLatestNotify() {
    return ResponseEntity.ok().body(notifyService.getLatestNotify());
  }

  @GetMapping("get/all/with/read/{id}/{level}")
  public ResponseEntity<List<NotifyWithReadDTO>> getAllNotifyWithReadState(
      @PathVariable Long id, @PathVariable Level level) {
    return ResponseEntity.ok().body(notifyService.getAllNotifyWithReadState(id, level));
  }

  @DeleteMapping("delete/{id}")
  @PreAuthorize(MANAGER_ACCESS)
  public void deleteNotify(@PathVariable Long id) {
    notifyService.deleteNotify(id);
  }

  @PatchMapping("read/{id}/{employeeId}")
  public void read(@PathVariable Long id, @PathVariable Long employeeId) {
    notifyService.markAsRead(id, employeeId);
  }

  @GetMapping("read/about/notify/{id}/{employeeId}")
  public ResponseEntity<Boolean> isRead(
      @PathVariable Long id, @PathVariable Long employeeId) {
    return ResponseEntity.ok().body(notifyService.isNotifyRead(id, employeeId));
  }

  @GetMapping("count/notify/{id}/{level}")
  public ResponseEntity<Long> getUnreadNotifyCount(
      @PathVariable Long id, @PathVariable Level level) {
    return ResponseEntity.ok().body(notifyService.countUnreadNotify(id, level));
  }

  @GetMapping("get/read/{employeeId}")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<List<Notify>> getNotifyListByReadEmployee(@PathVariable Long employeeId) {
    return ResponseEntity.ok().body(notifyService.getReadNotifyListByEmployee(employeeId));
  }

  @GetMapping("get/unread/{employeeId}/{level}")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<List<Notify>> getNotifyListByUnReadEmployee(@PathVariable Long employeeId, @PathVariable Level level) {
    return ResponseEntity.ok().body(notifyService.getUnreadNotifyListByEmployee(employeeId, level));
  }

  @GetMapping("get/unread/all/{employeeId}/{level}")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<List<Notify>> getAllNotifyListByUnReadEmployee(@PathVariable Long employeeId, @PathVariable Level level) {
    return ResponseEntity.ok().body(notifyService.getAllUnreadNotifyListByEmployee(employeeId, level));
  }
}
