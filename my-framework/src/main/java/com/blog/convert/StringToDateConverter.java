package com.blog.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 自定义数据类型转换器并注入容器，即可自动进行转换
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String s) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(s);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }
}
