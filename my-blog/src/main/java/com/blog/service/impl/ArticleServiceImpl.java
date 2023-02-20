package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.constants.SystemConstants;
import com.blog.entity.Article;
import com.blog.entity.Category;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import com.blog.service.CategoryService;
import com.framework.utils.RedisUtil;
import com.blog.vo.ArticleDetailVo;
import com.blog.vo.ArticleListVo;
import com.blog.vo.HotArticleVo;
import com.blog.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisUtil RedisUtil;

    @Override
    public List<HotArticleVo> hotArticle() {
//        用基础的querywrapper或者lambda
//        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>();
//        queryWrapper.eq("status", 0);

        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //文章状态为“非草稿”
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按浏览量降序排序
        lambdaQueryWrapper.orderByDesc(Article::getViewCount);
        //查看前10条
        Page<Article> page = new Page<>(1, 10);
        page(page, lambdaQueryWrapper);
        List<Article> records = page.getRecords();

//        bean拷贝，进行数据封装
        List<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article record : records) {
            //从redis中查询最新浏览量
            Integer viewCount = Integer.valueOf(RedisUtil.hget("viewCount", record.getId().toString()).toString());
            record.setViewCount(Long.valueOf(viewCount));

            HotArticleVo hotArticleVo = new HotArticleVo();
            BeanUtils.copyProperties(record, hotArticleVo);
            hotArticleVos.add(hotArticleVo);
        }
        return hotArticleVos;
    }

    @Override
    public PageVo articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> query = new LambdaQueryWrapper<>();
        //如果指定了categoryId，就要传入并查询对应文章
        query.eq(categoryId != null && categoryId > 0, Article::getCategoryId, categoryId);
        //文章状态为正式发布的
        query.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序排序
        query.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page(pageNum, pageSize);
        page(page, query);

        List<Article> articlesList = page.getRecords();
        articlesList.stream()
                .map(new Function<Article, Object>() {
                    @Override
                    public Article apply(Article article) {
                        Long categoryId = article.getCategoryId();
                        Category category = categoryService.getById(categoryId);
                        //或者用lombok的注解 @Accessors(chain = true)
                        //可以将set方法的返回值从void变为对象本身，就可以直接返回他
                        //可以进一步简化写法
                        article.setCategoryName(category.getName());
                        return article;
                    }
                })
                .collect(Collectors.toList());

        List<ArticleListVo> listVos = new ArrayList<>();
        for (Article record : page.getRecords()) {
            //从redis中查询最新浏览量
            Integer viewCount = Integer.valueOf(RedisUtil.hget("viewCount", record.getId().toString()).toString());
            record.setViewCount(Long.valueOf(viewCount));

            ArticleListVo articleListVo = new ArticleListVo();
            BeanUtils.copyProperties(record, articleListVo);
            listVos.add(articleListVo);
        }

        PageVo pageVo = new PageVo(listVos, page.getTotal());
        return pageVo;
    }

    @Override
    public ArticleDetailVo getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中查询最新浏览量
        Integer viewCount = Integer.valueOf(RedisUtil.hget("viewCount", id.toString()).toString());
        article.setViewCount(Long.valueOf(viewCount));

        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article, articleDetailVo);

        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null){
            articleDetailVo.setCategoryName(category.getName());
        }

        return articleDetailVo;
    }

    @Override
    public void updateViewCount(Long id) {
        //更新redis中对应文章的浏览量
        RedisUtil.hincr("viewCount", id.toString(), 1);
    }

}
