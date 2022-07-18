package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Category;
import com.blog.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-07-13 20:59:55
 */
public interface CategoryService extends IService<Category> {

    List<CategoryVo> getCategoryList();
}
