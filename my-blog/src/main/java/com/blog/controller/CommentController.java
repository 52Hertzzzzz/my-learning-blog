package com.blog.controller;

import com.blog.entity.Comment;
import com.blog.service.CommentService;
import com.blog.vo.PageVo;
import com.framework.constants.SystemConstants;
import com.framework.enums.AppHttpCodeEnum;
import com.framework.exception.SystemException;
import com.framework.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/***
 * 评论相关功能
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关功能接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /***
     * 获取文章评论列表
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/commentList")
    @ApiOperation(value = "文章评论列表", notes = "获取该文章的所有评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章ID"),
            @ApiImplicitParam(name = "pageNum", value = "页数"),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸")
    })
    public Result<?> commentList(Long articleId, Integer pageNum, Integer pageSize){
        PageVo pageVo = commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
        return Result.ok(pageVo);
    }

    /***
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping
    public Result<?> addComment(@RequestBody Comment comment){
//        校验评论内容不为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        Boolean aBoolean = commentService.addComment(comment);
        return Result.ok(aBoolean);
    }

    /***
     * 获取友链评论列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/linkCommentList")
    public Result<?> linkCommentList(Integer pageNum, Integer pageSize){
        PageVo pageVo = commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
        return Result.ok(pageVo);
    }

}
