package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.constants.SystemConstants;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import com.blog.service.UserService;
import com.framework.utils.SecurityUtils;
import com.blog.vo.CommentVo;
import com.blog.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-07-16 15:24:41
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public PageVo commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询文章根评论，先不查询子评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //先判断评论类型是否为文章评论，如果是再传入文章id
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getRootId, -1);

        //评论类型判断
        queryWrapper.eq(Comment::getType, commentType);
        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        //数据封装成Vo并用PageVo返回
        List<CommentVo> commentVos = toCommentVoList(page.getRecords());
        for (CommentVo commentVo : commentVos) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        PageVo pageVo = new PageVo(commentVos, page.getTotal());

        return pageVo;
    }

    @Override
    public Boolean addComment(Comment comment) {
        comment.setCreateBy(SecurityUtils.getUserId());
        boolean save = save(comment);
        return save;
    }

    //Comment → CommentVo转化方法
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : list) {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            //查询评论者用户名
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //查询被评论者用户名
            //要先判断是否有被评论，否则查询-1会出现空指针
            Long toCommentUserId = commentVo.getToCommentUserId();
            if (toCommentUserId != -1){
                String commentedNickName = userService.getById(toCommentUserId).getNickName();
                commentVo.setToCommentUserName(commentedNickName);
            }

            commentVos.add(commentVo);
        }

        return commentVos;
    }

    //查询根评论的子评论集合
    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);

        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);

        return commentVos;

    }

}
