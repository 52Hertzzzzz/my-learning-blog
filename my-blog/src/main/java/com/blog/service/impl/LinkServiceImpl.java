package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Link;
import com.blog.mapper.LinkMapper;
import com.blog.service.LinkService;
import com.blog.vo.LinkVo;
import com.framework.constants.SystemConstants;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-07-14 18:10:41
 */
@Slf4j
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 12, 60L, TimeUnit.SECONDS,
                                                                              new ArrayBlockingQueue<>(10240));

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public List<LinkVo> getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = list(queryWrapper);

        List<LinkVo> linkVoLink = new ArrayList<>();
        for (Link link : linkList) {
            LinkVo linkVo = new LinkVo();
            BeanUtils.copyProperties(link, linkVo);
            linkVoLink.add(linkVo);
        }

        return linkVoLink;
    }

    @Override
    public Mono<Link> insert() {
        final int i = RandomUtils.nextInt(100, 500);
        log.info("构建Mono Start");

        Flux.just(i)
            .flatMapIterable((Function<Integer, Iterable<List<Link>>>) integer -> {
                int v = i;
                List<List<Link>> res = Lists.newArrayList();
                List<Link> list = Lists.newArrayList();
                while (v > 0) {
                    log.info("{} 号构建成功", v);
                    Link build = Link.builder()
                                     .name(String.valueOf(v).concat("号链接"))
                                     .description(String.valueOf(v).concat("号链接"))
                                     .build();

                    list.add(build);
                    if (100 == list.size()) {
                        res.add(Lists.newArrayList(list));
                        list.clear();
                    }
                    v--;
                }

                return res;
            })
            .doOnNext(v -> {
                List<Link> v1 = v;
                log.info("当前数组长度为: {}", v1.size());
            })
            .doOnNext(v -> {
                log.info("开始当前批次批量插入");
                this.saveBatch(v);
            })
            .subscribeOn(Schedulers.parallel()).subscribe();

        Mono<Link> linkMono = Mono.just(i)
                                  .map(v -> {
                                      int k = 0;
                                      LinkedList<Link> list = Lists.newLinkedList();
                                      while (k < v && k < 500) {
                                          log.info("{} 号构建成功", v);
                                          Link build = Link.builder()
                                                           .name(String.valueOf(k).concat("号链接"))
                                                           .description(String.valueOf(k).concat("号链接"))
                                                           .build();

                list.add(build);
                k++;
            }

            return list;
        }).doOnNext(v -> {
            this.saveBatch(v, 1000);
        }).doOnNext(v -> log.info("Insert Success")).subscribeOn(Schedulers.parallel()).map(v -> new Link());

        log.info("构建Mono End");
        return linkMono;
    }

    @Override
    public void insert1() {
        final int i = RandomUtils.nextInt(100, 500);
        log.info("构建Flux Start");

        Flux.just(i)
            .flatMapIterable((Function<Integer, Iterable<List<Link>>>) integer -> {
                int v = i;
                List<List<Link>> res = Lists.newArrayList();
                List<Link> list = Lists.newArrayList();
                while (v > 0) {
                    log.info("{} 号构建成功", v);
                    Link build = Link.builder()
                                     .name(String.valueOf(v).concat("号链接"))
                                     .description(String.valueOf(v).concat("号链接"))
                                     .build();

                    list.add(build);
                    v--;
                    if (100 == list.size() || v == 0) {
                        res.add(Lists.newArrayList(list));
                        list.clear();
                    }
                }

                return res;
            }).doOnNext(v -> {
                List<Link> v1 = v;
                log.info("当前数组长度为: {}", v1.size());
            }).doOnNext(v -> {
                log.info("开始当前批次批量插入");
                this.saveBatch(v);
            }).subscribeOn(Schedulers.immediate()).subscribe();
    }

    @Override
    public Long mvcTest(String type, Long times) {
        List<Link> build = build(times);
        final long[] l = {0};
        Stopwatch stopwatch = Stopwatch.createStarted();
        build.stream().forEach(v -> executor.execute(() -> {
                this.save(v);
                l[0]++;
            }
        ));

        while (true) {
            if (times == l[0]) {
                log.info("Cost: {}", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
                break;
            }
        }

        return 1L;
//        return stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
    }

    @Override
    public Long reactorTest(String type, Long times) {
        List<Link> build = build(times);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Flux.fromIterable(build)
            .doOnNext(v -> this.save(v))
            .doFinally(new Consumer<SignalType>() {
                @Override
                public void accept(SignalType signalType) {
                    log.info("Cost: {}", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
                }
            })
            .subscribeOn(Schedulers.parallel())
            .subscribe();

        return 1L;
//        return stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
    }

    @Override
    public void test1() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("abc");
        boolean exists = bloomFilter.isExists();
        log.info("Bloom Filter exists: {}", exists);

        bloomFilter.tryInit(1000000, 0.03);
        boolean exists1 = bloomFilter.isExists();
        log.info("Bloom Filter exists: {}", exists1);
        bloomFilter.add("aaaaa");

        bloomFilter.tryInit(1000000, 0.03);
        boolean exists2 = bloomFilter.isExists();
        log.info("Bloom Filter exists: {}", exists2);

    }

    @Override
    public void test2() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("test1");
        bloomFilter.tryInit(1000000, 0.03);

        Stopwatch stopwatch = Stopwatch.createStarted();
        bloomFilter.contains("a");
        bloomFilter.add("a");
        bloomFilter.contains("b");
        bloomFilter.add("c");
        bloomFilter.contains("d");
        bloomFilter.add("e");
        bloomFilter.contains("f");
        bloomFilter.add("g");
        bloomFilter.contains("h");
        bloomFilter.add("i");
        bloomFilter.contains("j");
        bloomFilter.add("k");
        bloomFilter.contains("l");
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        log.info("Cost: {}", elapsed);
    }

    @Override
    public void test3() {

        //executor;
    }

    private List<Link> build(Long times) {
        List<Link> list = Lists.newArrayList();
        while (times > 0) {
            Link build = Link.builder()
                             .name(String.valueOf(times).concat("号链接"))
                             .description(String.valueOf(times).concat("号链接"))
                             .build();
            list.add(build);
            times--;
        }

        return list;
    }

}
