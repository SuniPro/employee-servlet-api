package com.taekang.employeeservletapi.service.auth;

import com.taekang.employeeservletapi.DTO.CustomEmployeeDTO;
import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final CustomEmployeeDetailService customEmployeeDetailService;
  private final JwtUtil jwtUtil;

  public JwtAuthFilter(CustomEmployeeDetailService customEmployeeDetailService, JwtUtil jwtUtil) {
    this.customEmployeeDetailService = customEmployeeDetailService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String token = authorizationHeader.substring(7);

      /* 토큰 유효성 검증*/
      if (jwtUtil.validateToken(token)) {
        String name = jwtUtil.getEmployeeName(token);
        String level = jwtUtil.getLevel(token);
        String department = jwtUtil.getDepartment(token);

        CustomEmployeeDTO dto = new CustomEmployeeDTO();
        dto.setName(name);
        dto.setLevel(Level.valueOf(level));
        dto.setDepartment(Department.valueOf(department));

        UserDetails userDetails = new CustomEmployeeDetails(dto);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
