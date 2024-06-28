package com.salat.consumer;

import com.salat.dto.User;
import java.time.LocalTime;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RabbitMQJsonConsumer {

    //@RabbitListener(queues = {"${rabbitmq.jsonqueue.name}"})
    public void consume(User user) {
        LocalTime time = LocalTime.now();
        log.info("Received from Queue: {} {}", user, time);
    }
}
