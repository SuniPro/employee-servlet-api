package com.taekang.employeeservletapi.controller;

import com.taekang.employeeservletapi.DTO.AbilityDTO;
import com.taekang.employeeservletapi.DTO.EmployeesAbilityDTO;
import com.taekang.employeeservletapi.DTO.WorkBalanceDTO;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.error.TokenNotValidateException;
import com.taekang.employeeservletapi.service.ReviewService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("review")
public class ReviewController {

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

  @GetMapping("get/ability/by/{id}")
  public ResponseEntity<AbilityDTO> getAbilityById(@PathVariable Long id) {
    return ResponseEntity.ok().body(reviewService.getAbilityByEmployeeId(id));
  }
}
