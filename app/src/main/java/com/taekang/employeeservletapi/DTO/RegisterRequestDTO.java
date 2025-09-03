package com.taekang.employeeservletapi.DTO;

import com.taekang.employeeservletapi.entity.employee.Department;
import com.taekang.employeeservletapi.entity.employee.Level;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequestDTO {

  private String name;

  private String password;

  private Department department;

  private String site;

  private Level level;

  private List<SiteWalletDTO> siteWalletList;
}
