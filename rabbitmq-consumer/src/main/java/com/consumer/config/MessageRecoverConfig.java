//package com.consumer.config;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.retry.MessageRecoverer;
//import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///***
// * 超过重试次数处理器
// */
//@Configuration
//public class MessageRecoverConfig {
//
//    //发送邮件死信队列信息
//    private static final String eMailDeadLetterExchange = "email.dead";
//    private static final String eMailDeadLetterQueue = "email.dead";
//    private static final String eMailDeadLetterRoutingKey = "email.dead";
//
//    @Bean
//    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
//        return new RepublishMessageRecoverer(rabbitTemplate, eMailDeadLetterExchange, eMailDeadLetterRoutingKey);
//    }
//
//}
