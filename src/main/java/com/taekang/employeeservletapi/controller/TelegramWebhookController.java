package com.taekang.employeeservletapi.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.taekang.employeeservletapi.DTO.telegram.TelegramChat;
import com.taekang.employeeservletapi.DTO.telegram.TelegramMessage;
import com.taekang.employeeservletapi.DTO.telegram.TelegramUpdate;
import com.taekang.employeeservletapi.entity.employee.Site;
import com.taekang.employeeservletapi.error.TokenNotValidateException;
import com.taekang.employeeservletapi.service.TelegramLinkService;
import com.taekang.employeeservletapi.service.auth.JwtUtil;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TelegramWebhookController {

  private final JwtUtil jwtUtil;

  @Value("${telegram.webhook-secret:}")
  private String webhookSecret;

  private static final String MANAGER_ACCESS =
          "hasAnyAuthority('LEVEL_DEVELOPER','LEVEL_ADMINISTRATOR','LEVEL_MANAGER')";

  private final TelegramLinkService telegramLinkService;
  private final TelegramBot telegramBot;

  @Autowired
  public TelegramWebhookController(
      TelegramLinkService telegramLinkService, TelegramBot telegramBot, JwtUtil jwtUtil) {
    this.telegramLinkService = telegramLinkService;
    this.telegramBot = telegramBot;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("${telegram.webhook-path:/telegram/webhook}")
  public ResponseEntity<Void> onUpdate(
      @RequestHeader(value = "X-Telegram-Bot-Api-Secret-Token", required = false) String secret,
      @RequestBody TelegramUpdate update) {

    log.info("✅ Webhook 호출됨 - secret: {}, update: {}", secret, update);

    if (StringUtils.hasText(webhookSecret) && !Objects.equals(secret, webhookSecret)) {
      return ResponseEntity.ok().build(); // 비정상 요청
    }

    if (update == null || update.getMessage() == null || update.getMessage().getChat() == null) {
      return ResponseEntity.ok().build();
    }

    TelegramMessage msg = update.getMessage();
    TelegramChat chat = msg.getChat();

    Long chatId = chat.getId();
    String username = chat.getUsername();
    String text = msg.getText();

    if (text != null && text.toLowerCase().startsWith("/start")) {
      String[] parts = text.trim().split("\\s+");
      if (parts.length < 2) {
        telegramBot.execute(new SendMessage(chatId, "ℹ️ 사이트에서 발급된 연결 링크로 시작해야 등록됩니다."));
        return ResponseEntity.ok().build();
      }

      String token = parts[1].trim();
      try {
        Site linked = telegramLinkService.consumeLinkTokenAndLink(token, chatId, username);
        telegramBot.execute(
            new SendMessage(chatId, "✅ 연결 완료! 이제 사이트 [" + linked.getSite() + "] 알림을 보내드려요."));
      } catch (TokenNotValidateException e) {
        telegramBot.execute(new SendMessage(chatId, "❌ 이미 다른 사이트에 연결된 계정이거나, 잘못되었거나 만료된 링크입니다."));
      } catch (Exception e) {
        telegramBot.execute(new SendMessage(chatId, "⚠️ 처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요."));
      }
      return ResponseEntity.ok().build();
    }

    telegramBot.execute(new SendMessage(chatId, "ℹ️ 이 봇은 사이트에서 발급한 링크로 시작해야 연결됩니다."));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/employee/link-token")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<Map<String, String>> issueLinkToken(
      @CookieValue("access-token") String token) {
    String site = jwtUtil.getSite(token);
    String url = telegramLinkService.issueLinkToken(site);
    return ResponseEntity.ok(Map.of("site", site, "link", url));
  }

  // (선택) 연결 해제
  @PostMapping("/employee/unlink")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<Void> unlink(@CookieValue("access-token") String token) {
    String site = jwtUtil.getSite(token);
    telegramLinkService.unlinkTelegram(site);
    return ResponseEntity.ok().build();
  }
}
