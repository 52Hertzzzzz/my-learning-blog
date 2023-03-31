package com.essearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.essearch.entity.EsUser;
import com.framework.utils.Result;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

@RestController
@RequestMapping("/user")
@Slf4j
public class ElasticSearchUserController {

    @Autowired
    private RestHighLevelClient client;

    /***
     * 插入user
     * @param user
     * @return
     */
    @PostMapping("/insertUserDetail")
    public Result<?> insertUserDetail(@RequestBody EsUser user) {
        //指定索引库名称进行操作
        IndexRequest indexRequest = new IndexRequest("user");
        indexRequest.source(JSONObject.toJSONString(user), XContentType.JSON);
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("插入user索引出现异常: {}", e);
            return Result.error("插入user索引出现异常");
        }

        return Result.ok("ok");
    }

    /***
     * 查询所有user
     * @return
     */
    @PostMapping("/queryAllUser")
    public Result<?> queryAllUser() {
        //指定索引库名称进行操作
        SearchRequest searchRequest = new SearchRequest("user");

        //组装查询条件并赋值
        SearchSourceBuilder search = new SearchSourceBuilder();
        //match_all
        MatchAllQueryBuilder builder = new MatchAllQueryBuilder();
        search.query(builder);
        searchRequest.source(search);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            HashMap<String, Object> resMap = Maps.newHashMapWithExpectedSize(2);
            LinkedList<EsUser> resList = Lists.newLinkedList();

            for (SearchHit hit : hits.getHits()) {
                String str = hit.getSourceAsString();
                log.info("hit: {}", str);
                EsUser esUser = JSONObject.parseObject(str, EsUser.class);
                resList.add(esUser);
            }

            resMap.put("count", hits.getTotalHits());
            resMap.put("data", resList);
            return Result.ok(resMap);
        } catch (IOException e) {
            log.error("查询user索引出现异常: {}", e);
            return Result.error("查询user索引出现异常");
        }
    }

    /***
     * 精确查询 + 基础分页
     * @param json
     * @return
     */
    @PostMapping("/queryUserPage")
    public Result<?> queryUserDetail(@RequestBody JSONObject json) {
        log.info("入参 -> {}", json);

        //指定索引库名称进行操作
        SearchRequest searchRequest = new SearchRequest("user");

        //组装查询条件并赋值
        SearchSourceBuilder search = new SearchSourceBuilder();
        //term
        TermQueryBuilder term = new TermQueryBuilder("username", json.getString("userName"));
        search.query(term);
        //分页参数
        Integer pageSize = json.getInteger("pageSize");
        Integer pageNo = (json.getInteger("pageNo") - 1) * pageSize;
        search.from(pageNo).size(pageSize);
        searchRequest.source(search);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            HashMap<String, Object> resMap = Maps.newHashMapWithExpectedSize(2);
            LinkedList<EsUser> resList = Lists.newLinkedList();

            for (SearchHit hit : hits) {
                String str = hit.getSourceAsString();
                log.info("hit: {}", str);
                EsUser esUser = JSONObject.parseObject(str, EsUser.class);
                resList.add(esUser);
            }

            resMap.put("total", hits.getTotalHits());
            resMap.put("data", resList);
            return Result.ok(resMap);
        } catch (IOException e) {
            log.error("精确查询user索引出现异常: {}", e);
            return Result.error("精确查询user索引出现异常");
        }
    }

    /***
     * 过滤器查询
     * filter效率比query要高，因为query需要计算文档得分再根据分数排序
     * 而filter只判断是否满足条件，因此大部分场景term都可以用filter替换
     * @param json
     * @return
     */
    @PostMapping("/queryUserLocation")
    public Result<?> queryUserLocation(@RequestBody JSONObject json) {
        log.info("入参 -> {}", json);
        String[] locations = json.getJSONArray("location").toArray(new String[]{});

        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder builder = new SearchSourceBuilder();
        //filter条件构建
        BoolQueryBuilder bool = new BoolQueryBuilder();
        TermsQueryBuilder terms = new TermsQueryBuilder("location", locations);
        bool.filter(terms);
        builder.query(bool);
        request.source(builder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            HashMap<String, Object> resMap = Maps.newHashMapWithExpectedSize(2);
            LinkedList<EsUser> resList = Lists.newLinkedList();

            for (SearchHit hit : hits) {
                String str = hit.getSourceAsString();
                log.info("hit: {}", str);
                EsUser esUser = JSONObject.parseObject(str, EsUser.class);
                resList.add(esUser);
            }

            resMap.put("total", hits.getTotalHits());
            resMap.put("data", resList);
            return Result.ok(resMap);
        } catch (IOException e) {
            log.error("按地区过滤查询user索引出现异常: {}", e);
            return Result.error("按地区过滤查询user索引出现异常");
        }
    }

    /***
     * 桶聚合，统计用户名出现次数
     * 指标聚合大部分为数值操作，如求最大、最小、平均值
     * @param json
     * @return
     */
    @PostMapping("/queryUserBucket")
    public Result<?> queryUserBucket(@RequestBody JSONObject json) {
        log.info("入参 -> {}", json);

        //聚合构建
        //AggregationBuilders.terms()

        return null;
    }
}
