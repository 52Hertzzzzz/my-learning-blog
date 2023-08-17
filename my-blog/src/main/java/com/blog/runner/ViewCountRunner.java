//package com.blog.runner;
//
//import com.blog.entity.Article;
//import com.blog.mapper.ArticleMapper;
//import com.framework.utils.RedisUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///***
// * 初始化自动执行任务
// */
//@Component
//public class ViewCountRunner implements CommandLineRunner {
//
//    @Resource
//    private ArticleMapper articleMapper;
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Override
//    public void run(String... args) {
//        //查询博客信息 id/viewCount
//        List<Article> articles = articleMapper.selectList(null);
//        Map<String, Integer> viewCountMap = articles.stream()
//                                                    .collect(Collectors.toMap(article -> article.getId().toString(),
//                                                                              article -> article.getViewCount()
//                                                                                                .intValue()));
//
//        //存储到redis中
//        redisUtil.hmset("viewCount", ((Map<String, Object>) ((Object) viewCountMap)));
//    }
//}
