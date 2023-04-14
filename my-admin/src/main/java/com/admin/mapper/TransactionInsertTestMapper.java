package com.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TransactionInsertTestMapper {

    @DS("master")
    @Insert("insert into `sg_tag` (name, remark) values (#{integer}, #{integer}) ")
    void insertMain(@Param("integer") int integer);

    @DS("master")
    @Select("select count(*) from `sg_tag`")
    int readMain();

    @DS("slave")
    @Select("select count(*) from `sg_tag`")
    int readSlave();

}
