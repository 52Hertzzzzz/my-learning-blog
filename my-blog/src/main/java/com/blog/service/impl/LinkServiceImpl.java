package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Link;
import com.blog.mapper.LinkMapper;
import com.blog.service.LinkService;
import com.blog.vo.LinkVo;
import com.framework.constants.SystemConstants;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
                                  })
                                  .doOnNext(v -> {
                                      this.saveBatch(v, 1000);
                                  })
                                  .doOnNext(v -> log.info("Insert Success"))
                                  .subscribeOn(Schedulers.parallel())
                                  .map(v -> new Link());

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
            })
            .doOnNext(v -> {
                List<Link> v1 = v;
                log.info("当前数组长度为: {}", v1.size());
            })
            .doOnNext(v -> {
                log.info("开始当前批次批量插入");
                this.saveBatch(v);
            })
            .subscribeOn(Schedulers.immediate())
            .subscribe();
    }

}
