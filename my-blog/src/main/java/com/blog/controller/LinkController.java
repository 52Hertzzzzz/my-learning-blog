package com.blog.controller;


import com.blog.entity.Link;
import com.blog.service.LinkService;
import com.blog.vo.LinkVo;
import com.framework.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/***
 * 友链相关功能
 */
@Slf4j
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /***
     * 获取所有审核通过的友链
     * @return
     */
    @GetMapping("/getAllLink")
    public Result<?> getAllLink() {
        List<LinkVo> allLink = linkService.getAllLink();
        return Result.ok(allLink);
    }

    @GetMapping("/insert")
    public Mono<Link> insert() {
        Mono<Link> mono = linkService.insert();
        log.info("Controller Mono返回完毕");
        return mono;
    }

    @GetMapping("/insert1")
    public Result<?> insert1() {
        linkService.insert1();
        log.info("Controller Flux 返回完毕");
        return Result.ok("ok");
    }

}
