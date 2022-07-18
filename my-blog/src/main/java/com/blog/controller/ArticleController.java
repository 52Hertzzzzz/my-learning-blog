package com.blog.controller;

import com.blog.service.ArticleService;
import com.blog.utils.Result;
import com.blog.vo.ArticleDetailVo;
import com.blog.vo.HotArticleVo;
import com.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * 文章相关功能
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /***
     * 获取前10条最热文章信息
     * @return
     */
    @GetMapping("/hotArticle")
    public Result<?> hotArticle(){
        List<HotArticleVo> hotArticleVo = articleService.hotArticle();
        return Result.ok(hotArticleVo);
    }

    /***
     * 分页查询文章
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/articleList")
    public Result<?> articleList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                 Long categoryId){
        PageVo pageVo = articleService.articleList(pageNum, pageSize, categoryId);
        return Result.ok(pageVo);
    }

    /***
     * 获取文章具体信息
     * @param id
     * @return
     */
    //这种形式获取参数，可以用pathvariable注解和占位符
    @GetMapping("/{id}")
    public Result<?> getArticleDetail(@PathVariable("id")Long id){
        ArticleDetailVo articleDetail = articleService.getArticleDetail(id);
        return Result.ok(articleDetail);
    }

    /***
     * 浏览量递增
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public Result<?> updateViewCount(@PathVariable("id") Long id){
        articleService.updateViewCount(id);
        return Result.ok();
    }

}
