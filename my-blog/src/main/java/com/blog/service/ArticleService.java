package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Article;
import com.blog.vo.ArticleDetailVo;
import com.blog.vo.HotArticleVo;
import com.blog.vo.PageVo;

import java.util.List;

public interface ArticleService extends IService<Article> {

    List<HotArticleVo> hotArticle();

    PageVo articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ArticleDetailVo getArticleDetail(Long id);

    void updateViewCount(Long id);
}
