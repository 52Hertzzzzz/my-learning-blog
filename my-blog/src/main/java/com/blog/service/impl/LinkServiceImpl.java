package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.constants.SystemConstants;
import com.blog.entity.Link;
import com.blog.vo.LinkVo;
import com.blog.mapper.LinkMapper;
import com.blog.service.LinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-07-14 18:10:41
 */
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
}
