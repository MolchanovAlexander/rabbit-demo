package com.salat.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.jsonqueue.name}")
    private String jsonQueueName;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Value("${rabbitmq.json.routing.key}")
    private String routingJsonKey;


    @Bean
    public Queue localSpringQueue() {
        return new Queue("spring-queue");
    }

    @Bean
    public Queue jsonQueue() {
        return new Queue(jsonQueueName);
    }

    @Bean
    public Queue externalJavaClientQueue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange messageExchangeFanout() {
        return new TopicExchange(exchangeName);
    }

//    @Bean
//    public Binding springQueueBinding() {
//        return BindingBuilder
//                .bind(localSpringQueue())
//                .to(messageExchangeFanout())
//                .with("")
//                .noargs();
//    }

    @Bean
    public Binding externalQueueBinding() {
        return BindingBuilder
                .bind(externalJavaClientQueue())
                .to(messageExchangeFanout())
                .with(routingKey);

    }

    @Bean
    public Binding jsonQueueBinding() {
        return BindingBuilder
                .bind(jsonQueue())
                .to(messageExchangeFanout())
                .with(routingJsonKey);

    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(converter());
//        return rabbitTemplate;
//    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("Message delivered successfully");
            } else {
                log.error("Message delivery failed: {}", cause);
            }
        });
        rabbitTemplate.setReturnsCallback(returned -> {
            log.warn("Message returned: {}", returned);
        });
        return rabbitTemplate;
    }

}
