package com.taekang.employeeservletapi.entity.employee;

import com.taekang.employeeservletapi.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Table(name = "site")
@Builder(toBuilder = true)
public class Site extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "site", unique = true, nullable = false)
  private String site;

  @Column(name = "telegram_username")  // 선택
  private String telegramUsername;

  @Column(name = "telegram_chat_id", unique = true)   // 발송 키
  private Long telegramChatId;

  @Column(name = "telegram_link_token") // 1회용/기한부 토큰(선택)
  private String telegramLinkToken;

  @Column(name = "insert_id", nullable = false)
  private String insertId;

  @Column(name = "update_id")
  private String updateId;

  @Column(name = "delete_id")
  private String deleteId;

  @Column(name = "delete_date_time")
  private LocalDateTime deleteDateTime;

  public Site linkedTelegram(Long chatId, String username) {
    return this.toBuilder()
            .telegramChatId(chatId)
            .telegramUsername(username)
            .telegramLinkToken(null) // 1회용 토큰 소모
            .build();
  }
}
