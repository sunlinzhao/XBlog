package com.slz.xblog.mapper;
import com.slz.xblog.vo.PostCountByDayVo;
import com.slz.xblog.vo.PostSearchVo;
import org.apache.ibatis.annotations.Param;

import com.slz.xblog.entity.PostEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slz.xblog.vo.PostInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author slz
 * @since 2024-10-25
 */
@Mapper
public interface PostMapper extends BaseMapper<PostEntity> {

    List<PostInfoVo> getPostListById(PostInfoVo postInfoVo);

    int updateSelective(PostEntity postEntity);

    PostInfoVo getOneById(String postId);

    List<PostInfoVo> getDraftListById(PostInfoVo setUserId);

    List<PostInfoVo> getPostViewRank();

    List<PostCountByDayVo> getPublishCount();

    int updateViewCount(@Param("postId") String postId);

    List<PostInfoVo> getPostSearchList(PostSearchVo postSearchVo);
}
