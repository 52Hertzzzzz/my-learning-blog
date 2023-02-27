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
//public class BuildMQConfig {
//
//    //发送邮件队列信息
//    private static final String seckillExchange = "my.order";
//    private static final String seckillQueue = "order.seckill";
//    private static final String seckillRoutingKey = "order.seckill";
//
//    //发送邮件死信队列信息
//    private static final String seckillDeadLetterExchange = "seckill.dead";
//    private static final String seckillDeadLetterQueue = "seckill.dead";
//    private static final String seckillDeadLetterRoutingKey = "seckill.dead";
//
//
//    @Bean
//    public TopicExchange topicExchange1() {
//        return new TopicExchange(seckillExchange);
//    }
//
//    //@Bean
//    //public Queue queue1() {
//    //    return new Queue(seckillQueue);
//    //}
//
//    //绑定死信队列
//    @SuppressWarnings("all")
//    @Bean
//    public Queue deadLetterBinding2() {
//        HashMap<String, Object> params = Maps.newHashMapWithExpectedSize(2);
//        //声明当前队列绑定的死信交换机
//        //params.put("x-dead-letter-exchange", seckillDeadLetterExchange);
//        //声明当前队列的死信路由键
//        //params.put("x-dead-letter-routing-key", seckillDeadLetterRoutingKey);
//
//        return QueueBuilder
//                .durable(seckillQueue)
//                .withArguments(params)
//                .deadLetterExchange(seckillDeadLetterExchange)
//                .deadLetterRoutingKey(seckillDeadLetterRoutingKey)
//                .build();
//    }
//
//    @Bean
//    public Binding topicBinding1(Queue deadLetterBinding2, TopicExchange topicExchange1) {
//        return BindingBuilder
//                .bind(deadLetterBinding2)
//                .to(topicExchange1)
//                .with(seckillRoutingKey);
//    }
//
//    @Bean
//    public TopicExchange deadLetterExchange() {
//        return new TopicExchange(seckillDeadLetterExchange);
//    }
//
//    @Bean
//    public Queue deadLetterQueue1() {
//        return new Queue(seckillDeadLetterQueue);
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
