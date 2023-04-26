//package com.consumer.config;
//
//import com.google.common.collect.Maps;
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//
//@Configuration
//public class RabbitMQConfig {
//
//    //发送邮件队列信息
//    private static final String eMailExchange = "my.order";
//    private static final String eMailQueue = "order.email";
//    private static final String eMailRoutingKey = "order.email";
//
//    //发送邮件死信队列信息
//    private static final String eMailDeadLetterExchange = "email.dead";
//    private static final String eMailDeadLetterQueue = "email.dead";
//    private static final String eMailDeadLetterRoutingKey = "email.dead";
//
//
//    @Bean
//    public TopicExchange topicExchange1() {
//        return new TopicExchange(eMailExchange);
//    }
//
//    //@Bean
//    //public Queue queue1() {
//    //    return new Queue(eMailQueue);
//    //}
//
//    //绑定死信队列
//    @Bean
//    public Queue deadLetterBinding2() {
//        HashMap<String, Object> params = Maps.newHashMapWithExpectedSize(2);
//        //声明当前队列绑定的死信交换机
//        params.put("x-dead-letter-exchange", eMailDeadLetterExchange);
//        //声明当前队列的死信路由键
//        params.put("x-dead-letter-routing-key", eMailDeadLetterRoutingKey);
//        return QueueBuilder.durable(eMailQueue).withArguments(params).build();
//    }
//
//    @Bean
//    public Binding topicBinding1(Queue deadLetterBinding2, TopicExchange topicExchange1) {
//        return BindingBuilder
//                .bind(deadLetterBinding2)
//                .to(topicExchange1)
//                .with(eMailRoutingKey);
//    }
//
//    @Bean
//    public TopicExchange deadLetterExchange() {
//        return new TopicExchange(eMailDeadLetterExchange);
//    }
//
//    @Bean
//    public Queue deadLetterQueue1() {
//        return new Queue(eMailDeadLetterQueue);
//    }
//
//    @Bean
//    public Binding deadLetterBinding1(Queue deadLetterQueue1, TopicExchange deadLetterExchange) {
//        return BindingBuilder
//                .bind(deadLetterQueue1)
//                .to(deadLetterExchange)
//                .with("#");
//    }
//
//}
