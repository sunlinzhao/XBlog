package com.slz.xblog.service;

import com.slz.xblog.entity.PostEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slz.xblog.vo.PostCountByDayVo;
import com.slz.xblog.vo.PostInfoVo;
import com.slz.xblog.vo.PostSearchVo;
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
public interface IPostService extends IService<PostEntity> {
    boolean uploadPost(PostInfoVo postInfoVo);

    List<PostInfoVo> getPostListById(PostInfoVo postInfoVo);

    boolean deletePost(PostInfoVo postInfoVo);

    boolean updatePost(PostInfoVo postInfoVo);

    PostInfoVo getOneById(String postId);

    List<PostInfoVo> getDraftListById(PostInfoVo setUserId);

    List<PostInfoVo> getPostViewRank();

    // 统计文章近30日发布数量
    List<PostCountByDayVo> getPublishCount();

    List<PostInfoVo> getPostSearchList(PostSearchVo postSearchVo);
}
