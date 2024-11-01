package com.slz.xblog.mapper;
import com.slz.xblog.vo.CommentVo;
import com.slz.xblog.vo.PostInfoVo;
import org.apache.ibatis.annotations.Param;

import com.slz.xblog.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author slz
 * @since 2024-10-26
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentEntity> {
    int deleteByPostId(@Param("postId") String postId);
    int countByPostId(@Param("postId") String postId);

    List<CommentVo> getCommentsByPostId(@Param("postId") String postId);
}
