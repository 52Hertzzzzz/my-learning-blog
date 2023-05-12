package com.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Select;

public interface DSTestMapper {

    @DS("master")
    @Select("SELECT database()")
    String m1();

    @DS("slave")
    @Select("SELECT database()")
    String m2();

    @DS("slaveTest")
    @Select("SELECT database()")
    String m3();

}
