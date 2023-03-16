package com.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TransactionTestMapper {

    @DS("slave")
    @Select("select name from sg_tag")
    List<String> test1();

    @DS("slaveTest")
    @Select("select name from test_table")
    List<String> test2();

    @DS("slave")
    @Select("select id from sg_tag")
    List<String> test3();

}
