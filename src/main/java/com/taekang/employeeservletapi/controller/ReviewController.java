package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.AbilityDTO;
import com.taekang.employeeservletapi.DTO.AbilityReviewDTO;
import com.taekang.employeeservletapi.DTO.EmployeesAbilityDTO;
import com.taekang.employeeservletapi.DTO.WorkBalanceDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.error.TokenNotValidateException;
import com.taekang.employeeservletapi.service.ReviewService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("review")
public class ReviewController {

  private static final String MANAGER_ACCESS =
      "hasAnyAuthority('LEVEL_CEO', 'LEVEL_COO', 'LEVEL_CFO', 'LEVEL_CIO', 'LEVEL_CTO','LEVEL_CDO',"
          + " 'LEVEL_MANAGER', 'LEVEL_OFFICEMANAGER', 'LEVEL_SENIORMANAGER')";

  private final ReviewService reviewService;
  private final JwtUtil jwtUtil;

  @Autowired
  public ReviewController(ReviewService reviewService, JwtUtil jwtUtil) {
    this.reviewService = reviewService;
    this.jwtUtil = jwtUtil;
  }

  @GetMapping("get/commutes/by/{id}")
  public ResponseEntity<List<WorkBalanceDTO>> getCommutesById(@PathVariable Long id) {
    return ResponseEntity.ok().body(reviewService.getWorkBalanceByEmployeeId(id));
  }

  @GetMapping("get/ability/set/by/{employeeId}")
  public ResponseEntity<EmployeesAbilityDTO> getAbilitySetById(
      @PathVariable Long employeeId, HttpServletRequest request) {
    String token = jwtUtil.getAccessTokenInCookie(request);

    boolean validationToken = jwtUtil.validateToken(token);
    if (!validationToken) {
      throw new TokenNotValidateException();
    }

    String level = jwtUtil.getLevel(token);

    return ResponseEntity.ok()
        .body(reviewService.getEmployeeAbility(Level.valueOf(level), employeeId));
  }

  @GetMapping("get/ability/target/employee/list/by/level/{level}/and/department/{department}")
  public ResponseEntity<Page<AbilityReviewDTO>> getAbilityTargetEmployeeList(
      @PathVariable Level level,
      @PathVariable Department department,
      @PageableDefault(size = 10, sort = "review_date", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok()
        .body(reviewService.getEmployeesAbilityReviews(level, department, pageable));
  }

  @GetMapping("get/ability/by/{id}")
  public ResponseEntity<AbilityDTO> getAbilityById(@PathVariable Long id) {
    return ResponseEntity.ok().body(reviewService.getAbilityByEmployeeId(id));
  }

  @PreAuthorize(MANAGER_ACCESS)
  @PostMapping("create/ability")
  public ResponseEntity<List<String>> createAbility(
      @RequestBody List<AbilityReviewDTO> abilityReviewDTOList) {
    return ResponseEntity.ok().body(reviewService.createAbility(abilityReviewDTOList));
  }
}
