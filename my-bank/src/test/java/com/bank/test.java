package com.bank;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
public class test {

    @Test
    public void test1() {
        ConfigurableApplicationContext run = SpringApplication.run(BankApplication.class);
        ConfigurableListableBeanFactory factory = run.getBeanFactory();
        String[] beanDefinitionNames = factory.getBeanDefinitionNames();
        List<String> list = Arrays.asList(beanDefinitionNames);
        Iterator<String> iterator = list.iterator();
        System.out.println("##############Beans################");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        //Object datasource = factory.getBean("datasource");
        //System.out.println(datasource.toString());
    }

}
