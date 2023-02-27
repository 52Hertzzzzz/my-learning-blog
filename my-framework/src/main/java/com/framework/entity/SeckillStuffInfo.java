package com.framework.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (SeckillStuffInfo)表实体类
 *
 * @author makejava
 * @since 2023-02-27 10:46:05
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("seckill_stuff_info")
public class SeckillStuffInfo extends AbstractMessage {

    @TableId
    private Long id;

    private String stuffId;

    private String name;

    private Double price;

    private Long stuffCount;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    Date updateTime;

}
