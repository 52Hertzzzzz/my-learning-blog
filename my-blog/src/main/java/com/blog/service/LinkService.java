package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Link;
import com.blog.vo.LinkVo;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-07-14 18:10:40
 */
public interface LinkService extends IService<Link> {

    List<LinkVo> getAllLink();

    Mono<Link> insert();

    void insert1();

}
