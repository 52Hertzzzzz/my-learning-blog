package com.blog.controller;

import com.blog.service.CategoryService;
import com.blog.vo.CategoryVo;
import com.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * 种类相关功能
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public Result<?> getCategoryList(){
        List<CategoryVo> categoryList = categoryService.getCategoryList();
        return Result.ok(categoryList);
    }

}
