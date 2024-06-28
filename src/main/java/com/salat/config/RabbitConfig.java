package com.salat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    public Queue localSpringQueue() {
        return new Queue("spring-queue");
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
}
