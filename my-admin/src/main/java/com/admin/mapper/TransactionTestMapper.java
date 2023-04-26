package com.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

public interface TransactionTestMapper {

    @DS("slave")
    @Select("select name from sg_tag")
    Cursor<String> test1();

    @DS("slaveTest")
    @Select("select name from test_table")
    Cursor<String> test2();

    @DS("slave")
    @Select("select id from sg_tag")
    Cursor<String> test3();

}
