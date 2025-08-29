package com.taekang.employeeservletapi.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.taekang.employeeservletapi.error.TokenNotValidateException;
import com.taekang.employeeservletapi.service.TelegramLinkService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@ConditionalOnProperty(name = "telegram.polling.enabled", havingValue = "false", matchIfMissing = false)
@RequiredArgsConstructor
public class TelegramLongPollingRunner implements ApplicationRunner {

    private final TelegramBot bot;
    private final TelegramLinkService linkService;

    private volatile boolean running = true;

    @Override
    public void run(ApplicationArguments args) {
        Thread t = new Thread(this::loop, "telegram-long-polling");
        t.setDaemon(true);
        t.start();
        log.info("Telegram long polling started.");
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        log.info("Telegram long polling stopping...");
    }

    private void loop() {
        int offset = 0;
        while (running) {
            try {
                GetUpdates req = new GetUpdates()
                        .limit(100)      // 한번에 최대 100개
                        .timeout(20)     // long polling (초)
                        .offset(offset);

                GetUpdatesResponse resp = bot.execute(req);
                List<Update> updates = resp.updates();
                if (updates == null || updates.isEmpty()) {
                    continue;
                }

                for (Update u : updates) {
                    offset = u.updateId() + 1;

                    if (u.message() == null || u.message().text() == null) continue;

                    String text = u.message().text().trim();
                    Long chatId = u.message().chat().id();
                    String username = u.message().chat().username();

                    if (text.toLowerCase(Locale.ROOT).startsWith("/start")) {
                        String[] parts = text.split("\\s+", 2);
                        if (parts.length < 2) {
                            bot.execute(new SendMessage(chatId, "ℹ️ 사이트에서 발급된 연결 링크로 시작해야 등록됩니다."));
                            continue;
                        }
                        String token = parts[1].trim();
                        try {
                            var site = linkService.consumeLinkTokenAndLink(token, chatId, username);
                            bot.execute(new SendMessage(chatId, "✅ 연결 완료! 이제 Icoins에서 [" + site.getSite() + "]로 입금되면 알림을 보내드려요."));
                            log.info("Linked chatId={} to site={}", chatId, site.getSite());
                        } catch (TokenNotValidateException e) {
                            bot.execute(new SendMessage(chatId, "❌ 이미 다른 사이트에 연결된 계정이거나, 잘못되었거나 만료된 링크입니다."));
                        } catch (Exception e) {
                            log.warn("Link error", e);
                            bot.execute(new SendMessage(chatId, "⚠️ 처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요."));
                        }
                    } else {
                        // 필요 시 기타 명령 처리를 여기서…
                        bot.execute(new SendMessage(chatId, "ℹ️ 이 봇은 사이트에서 발급한 링크로 시작해야 연결됩니다."));
                    }
                }
            } catch (Exception e) {
                log.warn("Polling loop error: {}", e.getMessage());
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            }
        }
    }
}