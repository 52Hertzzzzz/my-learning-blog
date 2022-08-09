package com.blog.controller;

import com.blog.service.CategoryService;
import com.blog.utils.ExcelExportUtils;
import com.blog.utils.Result;
import com.blog.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

/***
 * 种类相关功能
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /***
     * 获取种类列表
     * @return
     */
    @GetMapping("/getCategoryList")
    public Result<?> getCategoryList(){
        List<CategoryVo> categoryList = categoryService.getCategoryList();
        return Result.ok(categoryList);
    }

    @GetMapping("/exportExcel")
    public Result<?> exportExcel(HttpServletResponse response){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
//        StringBuilder builder = new StringBuilder(now.format(dateTimeFormatter));
//        String excelName = builder.append("章节表").toString();

        String excelName = now.format(dateTimeFormatter).concat("ExcelFile");
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("id", "编号");
        fieldMap.put("name", "名称");
        fieldMap.put("description", "描述");

        List<CategoryVo> categoryList = categoryService.getCategoryList();
        ExcelExportUtils.export(excelName, categoryList, fieldMap, response);

        return Result.ok("导出成功");
    }

}
