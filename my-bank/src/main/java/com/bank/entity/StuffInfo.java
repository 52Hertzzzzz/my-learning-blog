package com.bank.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (StuffInfo)表实体类
 *
 * @author makejava
 * @since 2023-02-14 16:35:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stuff_info")
public class StuffInfo  {

    @TableId
    private Long id;

    private String stuffId;
    
    private String name;
    
    private Double price;
    
    private Long amount;
    
    private Integer status;

}
