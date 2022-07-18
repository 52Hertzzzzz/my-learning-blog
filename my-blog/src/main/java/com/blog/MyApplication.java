package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//可以暂时禁用Security
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
@MapperScan("com.blog.mapper")
//启动定时任务
@EnableScheduling
//启动swagger
@EnableSwagger2
//@ConfigurationPropertiesScan
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
