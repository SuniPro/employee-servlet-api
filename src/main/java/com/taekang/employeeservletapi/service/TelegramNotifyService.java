package com.taekang.employeeservletapi.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.taekang.employeeservletapi.DTO.crypto.DepositNotifyDTO;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelegramNotifyService {
  private final TelegramBot telegramBot;

  @Autowired
  public TelegramNotifyService(TelegramBot telegramBot) {
    this.telegramBot = telegramBot;
  }

  public boolean sendDepositRequest(Long chatId, DepositNotifyDTO depositNotifyDTO) {
    String text = formatMarkdown(depositNotifyDTO);

    LinkPreviewOptions lpo = new LinkPreviewOptions().isDisabled(true);

    SendMessage req =
        new SendMessage(chatId, text).linkPreviewOptions(lpo).parseMode(ParseMode.Markdown);

    SendResponse res = telegramBot.execute(req);
    if (!res.isOk()) {
      log.warn("Telegram send failed: {}", res.description());
    }
    return res.isOk();
  }

  private String formatMarkdown(DepositNotifyDTO dto) {
    // 보기 좋은 숫자/시간 포맷
    String realAmount = dto.getRealAmount().stripTrailingZeros().toPlainString();
    String amount = dto.getAmount().stripTrailingZeros().toPlainString();
    String krw = NumberFormat.getInstance(Locale.KOREA).format(dto.getKrwAmount());
    String when = dto.getRequestedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // 인덴트 없이 깔끔하게 (텔레그램 Markdown: *, _, ` 만 사용 권장)
    return "*입금 알림*\n"
        + "*이메일:* `"
        + dto.getEmail()
        + "`\n"
        + "*지갑주소:* `"
        + dto.getFromAddress()
        + "`\n"
        + "*실제 보낸 금액 ("
        + dto.getCryptoType()
        + " / 개수):* `"
        + realAmount
        + "`\n"
        + "*요청 금액 ("
        + dto.getCryptoType()
        + " / 개수):* `"
        + amount
        + "`\n"
        + "*요청 금액 (원 / ₩):* `"
        + krw
        + "`\n"
        + "*요청시각:* `"
        + when
        + "`";
  }
}
