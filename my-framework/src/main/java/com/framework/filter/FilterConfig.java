package com.framework.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 注册过滤器
 */
@Configuration
public class FilterConfig {

    /***
     * Body输入流复制过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new BodyDuplicateFilter());
        bean.addUrlPatterns("/pay/addOrder");
        bean.setOrder(1);

        return bean;
    }
}
