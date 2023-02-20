package com.blog.runner;

import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.framework.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * 初始化自动执行任务
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;

    @Autowired
    private RedisUtil RedisUtil;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id/viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {
                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Integer>() {
                    @Override
                    public Integer apply(Article article) {
                        return article.getViewCount().intValue();
                    }
                }));

        //存储到redis中
        RedisUtil.hmset("viewCount", ((Map<String, Object>) ((Object) viewCountMap)));


    }
}
