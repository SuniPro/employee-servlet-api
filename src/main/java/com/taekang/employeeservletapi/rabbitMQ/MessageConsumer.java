package com.taekang.employeeservletapi.rabbitMQ;

import com.taekang.employeeservletapi.DTO.crypto.DepositNotifyDTO;
import com.taekang.employeeservletapi.entity.employee.Site;
import com.taekang.employeeservletapi.error.CannotFoundSiteException;
import com.taekang.employeeservletapi.repository.employee.SiteRepository;
import com.taekang.employeeservletapi.service.TelegramNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumer {

    private final SiteRepository siteRepository;
    private final TelegramNotifyService telegramNotifyService;

    @Autowired
    public MessageConsumer(SiteRepository siteRepository, TelegramNotifyService telegramNotifyService) {
        this.siteRepository = siteRepository;
        this.telegramNotifyService = telegramNotifyService;
    }

    @RabbitListener(queues = "${rabbitmq.deposit.request.queue}")
    public void receiveDepositMessage(DepositNotifyDTO message) {
        log.info("Received deposit message: {}", message.toString());

        Site site = siteRepository.findBySite(message.getSite()).orElseThrow(CannotFoundSiteException::new);

        boolean ok = telegramNotifyService.sendDepositRequest(site.getTelegramChatId(), message);

        if (!ok){
            log.error("입금 요청 알림 실패");
        }
    }
}
