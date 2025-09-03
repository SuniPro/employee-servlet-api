package com.taekang.employeeservletapi.service.auth;

import com.taekang.employeeservletapi.DTO.CustomEmployeeDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomEmployeeDetails(CustomEmployeeDTO customEmployeeDTO) implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<String> roles = new ArrayList<>();

    roles.add("LEVEL_" + customEmployeeDTO.getLevel().toString());

    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return "";
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
