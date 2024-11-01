package com.slz.xblog.service;

import com.slz.xblog.entity.TagEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slz.xblog.vo.TagVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author slz
 * @since 2024-10-25
 */
public interface ITagService extends IService<TagEntity> {
    List<TagVo> getTagPostsNum();
}
