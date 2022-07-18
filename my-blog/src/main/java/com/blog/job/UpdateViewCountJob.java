package com.blog.job;

import com.blog.entity.Article;
import com.blog.service.ArticleService;
import com.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

//    加入@Scheduled注解，表示该方法为要执行的代码
//    cron属性为定时规则
    @Scheduled(cron = "0/30 * * * * ?")
    public void updateViewCount(){

        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("viewCount");
        List<Article> articles = viewCountMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()),
                        entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新数据库
        articleService.updateBatchById(articles);

    }

}
