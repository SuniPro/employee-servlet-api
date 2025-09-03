package com.taekang.employeeservletapi.rabbitMQ;

import com.taekang.employeeservletapi.DTO.crypto.DepositSentApprovalNotifyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageProducer {

  @Value("${rabbitmq.transaction.exchange}")
  private String transactionExchangeName;

  @Value("${rabbitmq.deposit.approval.routing}")
  private String depositApprovalRoutingKey;

  private final RabbitTemplate rabbitTemplate;

  @Autowired
  public MessageProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendDepositSendMessage(DepositSentApprovalNotifyDTO message) {
    log.info("Sent deposit message: {}", message.toString());
    rabbitTemplate.convertAndSend(transactionExchangeName, depositApprovalRoutingKey, message);
  }
}
