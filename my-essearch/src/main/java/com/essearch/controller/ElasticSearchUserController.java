package com.essearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.essearch.entity.EsUser;
import com.framework.utils.Result;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class ElasticSearchUserController {

    @Autowired
    private RestHighLevelClient client;

    /***
     * 创建索引
     * @param json
     * @return
     */
    @PostMapping("/createIndex")
    public Result<?> insertUserDetail(@RequestBody JSONObject json) {
        if (json.isEmpty()) {
            return Result.error("请指定索引信息");
        }
        if (Strings.isNullOrEmpty(json.getString("shards"))) {
            return Result.error("请自定义分片信息");
        }
        if (Strings.isNullOrEmpty(json.getString("replica"))) {
            return Result.error("请自定义分片信息");
        }
        String indexName = json.getString("indexName");
        if (Strings.isNullOrEmpty(indexName)) {
            return Result.error("请设置索引名称");
        }

        //创建请求
        CreateIndexRequest request = new CreateIndexRequest(indexName);

        //配置分片信息
        Settings setting = Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
                //指定索引默认分词器
                //.put("index.analysis.analyzer.default.type", "ik_max_word");
                .build();
        request.settings(setting);

        //配置映射信息
        String mappingString = json.fluentRemove("shards")
                .fluentRemove("replica")
                .fluentRemove("indexName")
                .toString();
        request.mapping(mappingString, XContentType.JSON);
        //这种方法在组装映射属性时太复杂，不推荐
        //LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
        //json.entrySet().forEach(a -> {
        //    String key = a.getKey();
        //    String value = a.getValue().toString();
        //    map.put(key, value);
        //});

        try {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            boolean acknowledged = response.isAcknowledged();
            boolean shardsAcknowledged = response.isShardsAcknowledged();
            //boolean fragment = response.isFragment();
            if (acknowledged && shardsAcknowledged) {
                return Result.ok("ok");
            } else {
                return Result.error("创建索引出现异常");
            }
        } catch (IOException e) {
            log.error("创建索引 {} 出现异常: {}", indexName, e);
            return Result.error("创建索引出现异常");
        }
    }

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

        //更新文档
        //UpdateRequest updateRequest = new UpdateRequest();
        //updateRequest.id(id);
        //updateRequest.doc();
        //client.update(updateRequest, RequestOptions.DEFAULT);

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
     * 桶聚合，统计用户地区
     * 指标聚合大部分为数值操作，如求最大、最小、平均值
     * @param json
     * @return
     */
    @PostMapping("/queryUserLocationAggs")
    public Result<?> queryUserLocationAggs(@RequestBody(required = false) JSONObject json) {
        log.info("入参 -> {}", json);

        SearchRequest request = new SearchRequest();
        request.indices("user");
        SearchSourceBuilder builder = new SearchSourceBuilder();

        //如指定地区则返回指定地区
        //未指定则返回所有地区
        String location = json.getString("location");
        if (!Strings.isNullOrEmpty(location)) {
            log.info("查询指定地区");
            BoolQueryBuilder bool = new BoolQueryBuilder();
            TermQueryBuilder term = new TermQueryBuilder("location.keyword", location);
            bool.filter(term);
            builder.query(bool);
        }

        //bucket聚合构建，词频统计
        TermsAggregationBuilder agg = AggregationBuilders.terms("location").field("location.keyword");
        builder.aggregation(agg);
        request.source(builder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //需要类型转换 Aggregation -> 具体实现类Terms，获取buckets
            //Aggregation aggregation = response.getAggregations().get("location");
            Terms aggregation = response.getAggregations().get("location");

            List<Aggregation> aggregations = response.getAggregations().asList();
            log.info("agg -> Name: {}", aggregation.getName());
            log.info("agg -> Type: {}", aggregation.getType());
            log.info("agg -> MetaData: {}", aggregation.getBuckets());

            SearchHits hits = response.getHits();
            HashMap<String, Object> resMap = Maps.newHashMapWithExpectedSize(3);
            LinkedList<EsUser> resList = Lists.newLinkedList();
            LinkedHashMap<String, Map<String, Object>> aggList = Maps.newLinkedHashMapWithExpectedSize(1);
            String source = null;
            Terms terms = null;
            EsUser esUser = null;
            HashMap<String, Object> bucketMap = null;

            for (SearchHit hit : hits) {
                source = hit.getSourceAsString();
                log.info("hit: {}", source);
                esUser = JSONObject.parseObject(source, EsUser.class);
                resList.add(esUser);
            }

            for (int i = 0; i < aggregations.size(); i++) {
                terms = (Terms) aggregations.get(i);
                bucketMap = Maps.newHashMapWithExpectedSize(1);
                for (Terms.Bucket bucket : terms.getBuckets()) {
                    bucketMap.put(bucket.getKeyAsString(), bucket.getDocCount());
                }
                aggList.put(terms.getName(), bucketMap);
            }

            resMap.put("total", hits.getTotalHits());
            resMap.put("data", resList);
            resMap.put("aggs", aggList);
            return Result.ok(resMap);
        } catch (IOException e) {
            log.error("桶聚合user索引出现异常: {}", e);
            return Result.error("桶聚合user索引出现异常");
        }
    }
}
