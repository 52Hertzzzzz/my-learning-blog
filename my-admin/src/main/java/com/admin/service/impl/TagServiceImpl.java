package com.admin.service.impl;

import com.admin.entity.Tag;
import com.admin.mapper.TagMapper;
import com.admin.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 标签(SgTag)表服务实现类
 *
 * @author makejava
 * @since 2022-07-25 14:22:14
 */
@Service("sgTagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
