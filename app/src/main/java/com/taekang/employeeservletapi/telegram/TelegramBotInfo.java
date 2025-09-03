package com.taekang.employeeservletapi.telegram;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotInfo {
  private final TelegramBot bot;
  private volatile String username;

  @Autowired
  public TelegramBotInfo(TelegramBot bot) {
    this.bot = bot;
  }

  @PostConstruct
  public void init() {
    var resp = bot.execute(new com.pengrad.telegrambot.request.GetMe());
    if (!resp.isOk() || resp.user() == null || resp.user().username() == null) {
      throw new IllegalStateException("Telegram getMe failed: " + resp.description());
    }
    username = resp.user().username();
  }

  public String username() {
    return username;
  }
}
