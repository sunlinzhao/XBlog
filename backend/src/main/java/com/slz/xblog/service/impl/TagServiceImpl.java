package com.slz.xblog.service.impl;

import com.slz.xblog.entity.TagEntity;
import com.slz.xblog.mapper.TagMapper;
import com.slz.xblog.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slz.xblog.vo.TagVo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author slz
 * @since 2024-10-25
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity> implements ITagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    @Cacheable(value = "tagPostsNum", key = "'tagPostsNum'")
    public List<TagVo> getTagPostsNum() {
        return tagMapper.getTagPostsNum();
    }
}
