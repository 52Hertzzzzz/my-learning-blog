package com.admin.config;

import com.admin.entity.TestPerson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestPersonConfig {

    @Bean
    public TestPerson testPerson1() {
        TestPerson liuzl = new TestPerson("liuzl", 22, "Xi'an");
        return liuzl;
    }

    @Bean
    public TestPerson testPerson2() {
        TestPerson liuzl = new TestPerson("guizy", 21, "Xi'an");
        return liuzl;
    }

}
