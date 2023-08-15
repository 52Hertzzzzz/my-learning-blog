package com.framework.config;

import com.framework.constants.RedissonPropertiesConstant;
import com.framework.constants.RedissonPropertiesConstantDev;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedissonConfig implements ApplicationContextAware {

    private static final String DEV = "dev";

    private ApplicationContext context;

    @Autowired
    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private Config initConfig() {
        //environment.getProperty();

        log.info("===================Redisson初始化开始===================");
        //Redisson配置文件
        Config config = new Config();
        //设置Codec序列化方式
        config.setCodec(new StringCodec())
                .useMasterSlaveServers()
                .setMasterAddress(RedissonPropertiesConstant.masterNode)
                .setMasterConnectionMinimumIdleSize(RedissonPropertiesConstant.minIdleSize)
                .setMasterConnectionPoolSize(RedissonPropertiesConstant.maxPoolSize)
                .addSlaveAddress(RedissonPropertiesConstant.slaveNode)
                .setSlaveConnectionMinimumIdleSize(RedissonPropertiesConstant.minIdleSize)
                .setSlaveConnectionMinimumIdleSize(RedissonPropertiesConstant.maxPoolSize)
                .setReadMode(ReadMode.MASTER_SLAVE);
        return config;
    }

    private Config initConfigDev() {
        log.info("===================Redisson初始化开始===================");
        //Redisson配置文件
        Config config = new Config();

        if (DEV.equals(environment.getActiveProfiles()[0])) {
            //设置Codec序列化方式
            config.setCodec(new StringCodec())
                  .useSingleServer()
                  .setAddress(RedissonPropertiesConstantDev.masterNode)
                  .setConnectionMinimumIdleSize(RedissonPropertiesConstantDev.minIdleSize)
                  .setConnectionPoolSize(RedissonPropertiesConstantDev.maxPoolSize);
        } else {
            //设置Codec序列化方式
            config.setCodec(new StringCodec())
                  .useSingleServer()
                  .setAddress(RedissonPropertiesConstant.masterNode)
                  .setConnectionMinimumIdleSize(RedissonPropertiesConstant.minIdleSize)
                  .setConnectionPoolSize(RedissonPropertiesConstant.maxPoolSize);
        }

        return config;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = this.initConfigDev();
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
        RedissonConnectionFactory factory = new RedissonConnectionFactory(redissonClient);
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedissonConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();

        log.info("===================Redisson初始化结束===================");
        return template;
    }

}
