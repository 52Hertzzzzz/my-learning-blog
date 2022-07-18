package com.blog.controller;


import com.blog.service.LinkService;
import com.blog.utils.Result;
import com.blog.vo.LinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * 友链相关功能
 */
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
    public Result<?> getAllLink(){
        List<LinkVo> allLink = linkService.getAllLink();
        return Result.ok(allLink);
    }

}
