package com.salat.controller;

import com.salat.dto.Message;
import com.salat.publisher.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessController {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProducer rabbitMQProducer;

    @PostMapping
    public void postMessage(@RequestBody Message message) {
        log.info("Received HTTP req with mes: {}", message.getBody());
        rabbitTemplate.convertAndSend("text-fanout", "", message.getBody());

    }

    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("dniwe") String message) {
        rabbitMQProducer.sendMessage(message);
        return ResponseEntity.ok("Dno sent to rabbitMQ...........");
    }
}
