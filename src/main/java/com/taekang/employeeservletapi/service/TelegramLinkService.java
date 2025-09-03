package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.entity.employee.Site;
import com.taekang.employeeservletapi.error.AlreadyTelegramConnectException;
import com.taekang.employeeservletapi.error.TokenNotValidateException;
import com.taekang.employeeservletapi.repository.employee.SiteRepository;
import com.taekang.employeeservletapi.telegram.TelegramBotInfo;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TelegramLinkService {

    private final SiteRepository siteRepository;
    private final TelegramBotInfo botInfo;

    @Autowired
    public TelegramLinkService(SiteRepository siteRepository, TelegramBotInfo botInfo) {
        this.siteRepository = siteRepository;
        this.botInfo = botInfo;
    }

    @Transactional
    public String issueLinkToken(String siteCode) {
        Site site = siteRepository.findBySite(siteCode)
                .orElseThrow(() -> new IllegalArgumentException("site not found"));

        String token = UUID.randomUUID().toString().replace("-", "");

        siteRepository.save(site.toBuilder().telegramLinkToken(token).build());

        return "https://t.me/" + botInfo.username() + "?start=" + token;
    }

    @Transactional
    public Site consumeLinkTokenAndLink(String token, Long chatId, String username) {
        Site site = siteRepository.findByTelegramLinkToken(token)
                .orElseThrow(TokenNotValidateException::new);

        if (Objects.equals(site.getTelegramChatId(), chatId)) {
            throw new AlreadyTelegramConnectException();
        }

        if (siteRepository.existsByTelegramChatId(chatId)){
            throw new TokenNotValidateException();
        }

        Site updated = site.toBuilder()
                .telegramChatId(chatId)
                .telegramUsername(username) // 최신 username으로 업데이트(선택)
                .telegramLinkToken(null)    // 1회용 토큰 소모
                .build();

        return siteRepository.save(updated);
    }

    @Transactional
    public void unlinkTelegram(String siteCode) {
        Site site = siteRepository.findBySite(siteCode)
                .orElseThrow(() -> new IllegalArgumentException("site not found"));

        siteRepository.save(site.toBuilder()
                .telegramChatId(null)
                .build());
    }
}
