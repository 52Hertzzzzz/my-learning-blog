package com.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange topicExchange1() {
        return new TopicExchange("my.order");
    }

    @Bean
    public Queue queue1() {
        return new Queue("order.all");
    }

    @Bean
    public Binding topicBinding1(Queue queue1, TopicExchange topicExchange1) {
        return BindingBuilder
                .bind(queue1)
                .to(topicExchange1)
                .with("order.*");
    }

}
