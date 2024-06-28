package com.salat.publisher;

import com.salat.dto.User;
import java.time.LocalTime;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RabbitMQJsonProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.json.routing.key}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(User user) {
        LocalTime time = LocalTime.now();
        log.info(" sent user to queue - {} {}", user, time);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, user);
    }
}
