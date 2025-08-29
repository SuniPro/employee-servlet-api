package com.taekang.employeeservletapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.transaction.exchange}")
    private String transactionExchangeName;

    @Value("${rabbitmq.deposit.request.queue}")
    private String depositRequestQueue;

    @Value("${rabbitmq.deposit.approval.queue}")
    private String depositApprovalQueue;

    @Value("${rabbitmq.deposit.request.routing}")
    private String depositRequestRoutingKey;

    @Value("${rabbitmq.deposit.approval.routing}")
    private String depositApprovalRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(transactionExchangeName);
    }

    @Bean
    public Queue depositRequestQueue() {
        return new Queue(depositRequestQueue);
    }

    @Bean
    public Queue depositApprovalQueue() {
        return new Queue(depositApprovalQueue);
    }

    @Bean
    public Binding depositRequestBinding(Queue depositRequestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(depositRequestQueue).to(exchange).with(depositRequestRoutingKey);
    }

    @Bean
    public Binding depositApprovalBinding(Queue depositApprovalQueue, TopicExchange exchange) {
        return BindingBuilder.bind(depositApprovalQueue).to(exchange).with(depositApprovalRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        // Jackson ObjectMapper를 사용하여 JSON 메시지를 변환하는 컨버터를 생성합니다.
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        // RabbitAdmin을 Bean으로 등록하면, 애플리케이션 시작 시
        // 같은 설정 파일에 있는 모든 Exchange, Queue, Binding Bean들을
        // RabbitMQ 서버에 자동으로 생성해 줍니다.
        return new RabbitAdmin(connectionFactory);
    }
}
