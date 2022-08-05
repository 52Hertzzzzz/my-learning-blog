package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final Logger logger = LoggerFactory.getLogger(MyApplication.class);


    public static void main(String[] args) {

        SpringApplication.run(MyApplication.class, args);
        logger.info("\n" + "Welcome :3" + "\n" +
                "   _____ _          _ _            \n" +
                "  / ____| |        | | |           \n" +
                " | (___ | |__   ___| | |_ ___ _ __ \n" +
                "  \\___ \\| '_ \\ / _ \\ | __/ _ \\ '__|\n" +
                "  ____) | | | |  __/ | ||  __/ |   \n" +
                " |_____/|_| |_|\\___|_|\\__\\___|_|   \n" +
                "                                   \n" +
                "                                   ");

//        logger.info(SpringBootVersion.getVersion());

//        ConfigurableApplicationContext run = SpringApplication.run(MyApplication.class, args);
//        ConfigurableListableBeanFactory beanFactory = run.getBeanFactory();
//        ArticleController articleController = beanFactory.getBean(ArticleController.class);
//        ArticleController articleController = (ArticleController) beanFactory.getBean("articleController");
//        Result<?> result = articleController.articleList(1, 10, 1L);

//        System.out.println(result);

    }

}
