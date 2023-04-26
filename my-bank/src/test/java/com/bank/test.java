package com.bank;

import com.framework.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class test {

    @Test
    public void test1() {
        ConfigurableApplicationContext run = SpringApplication.run(BankApplication.class);
        ConfigurableListableBeanFactory factory = run.getBeanFactory();
        RedisUtil redisUtil = factory.getBean(RedisUtil.class);
        RedisTemplate redisTemplate = (RedisTemplate) factory.getBean("redisTemplate");
        RedissonClient redissonClient = factory.getBean(RedissonClient.class);

        Map<Object, Object> map1 = redisUtil.hmget("SeckillStuffCount");
        Map map2 = redisTemplate.opsForHash().entries("SeckillStuffCount");
        RMap<Object, Object> map3 = redissonClient.getMap("SeckillStuffCount");
        String string = map3.entrySet().toString();

        log.info(map1.toString());
        log.info(map2.toString());
        log.info(string);
    }

    @Test
    public void test2() {
        ConfigurableApplicationContext run = SpringApplication.run(BankApplication.class);
        ConfigurableListableBeanFactory factory = run.getBeanFactory();

        String[] beanDefinitionNames = factory.getBeanDefinitionNames();
        List<String> list = Arrays.asList(beanDefinitionNames);
        Iterator<String> iterator = list.iterator();
        System.out.println("##############Beans################");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    public void test3() {
        ConfigurableApplicationContext run = SpringApplication.run(BankApplication.class);
        ConfigurableListableBeanFactory factory = run.getBeanFactory();
        RedisUtil redisUtil = factory.getBean(RedisUtil.class);
        RedisTemplate redisTemplate = (RedisTemplate) factory.getBean("redisTemplate");
        RedissonClient redissonClient = factory.getBean(RedissonClient.class);

        String key = "RedissonTest";
        redisUtil.hset(key, "redisUtil", 1);
        redisTemplate.opsForHash().put(key, "redisTemplate", 2);
        redissonClient.getMap(key).put("redissonClient", 3);
    }

}
