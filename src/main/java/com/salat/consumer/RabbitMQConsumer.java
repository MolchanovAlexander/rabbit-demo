package com.salat.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RabbitMQConsumer {

   // @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message) {
        log.info("Received from Queue: {}", message);

    }
}
