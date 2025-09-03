package com.taekang.employeeservletapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIsSendDTO {
  private Long id;

  @JsonProperty("isSend")
  private boolean isSend;
}
