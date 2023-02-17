//package com.consumer.config;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.retry.MessageRecoverer;
//import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MessageRecoverConfig {
//
//    @Bean
//    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
//        return new RepublishMessageRecoverer(rabbitTemplate, );
//    }
//
//}
