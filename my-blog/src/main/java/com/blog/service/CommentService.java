package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Comment;
import com.blog.vo.PageVo;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-07-16 15:24:41
 */
public interface CommentService extends IService<Comment> {

    PageVo commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    Boolean addComment(Comment comment);
}
