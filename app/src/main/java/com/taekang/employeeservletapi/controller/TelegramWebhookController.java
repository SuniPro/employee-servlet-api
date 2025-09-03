package com.taekang.employeeservletapi.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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
      "hasAnyAuthority('LEVEL_ADMINISTRATOR','LEVEL_MANAGER')";

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
      @RequestBody Update update) {
    if (StringUtils.hasText(webhookSecret)) {
      if (!Objects.equals(secret, webhookSecret)) {
        // 비정상 요청은 조용히 무시
        return ResponseEntity.ok().build();
      }
    }

    var msg = update.message();
    if (msg == null) return ResponseEntity.ok().build();

    Long chatId = msg.chat().id();
    String username = msg.chat().username();
    String text = msg.text();

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
        // 중복 chatId or 잘못된 토큰
        telegramBot.execute(new SendMessage(chatId, "❌ 이미 다른 사이트에 연결된 계정이거나, 잘못되었거나 만료된 링크입니다."));
      } catch (Exception e) {
        telegramBot.execute(new SendMessage(chatId, "⚠️ 처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요."));
      }
      return ResponseEntity.ok().build();
    }

    telegramBot.execute(new SendMessage(chatId, "ℹ️ 이 봇은 사이트에서 발급한 링크로 시작해야 연결됩니다."));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/link-token")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<Map<String, String>> issueLinkToken(
      @CookieValue("access-token") String token) {
    String site = jwtUtil.getSite(token);
    String url = telegramLinkService.issueLinkToken(site);
    return ResponseEntity.ok(Map.of("site", site, "link", url));
  }

  // (선택) 연결 해제
  @PostMapping("/unlink")
  @PreAuthorize(MANAGER_ACCESS)
  public ResponseEntity<Void> unlink(@CookieValue("access-token") String token) {
    String site = jwtUtil.getSite(token);
    telegramLinkService.unlinkTelegram(site);
    return ResponseEntity.ok().build();
  }
}
