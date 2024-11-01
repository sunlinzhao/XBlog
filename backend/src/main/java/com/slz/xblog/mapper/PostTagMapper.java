package com.slz.xblog.mapper;

import com.slz.xblog.entity.PostTagEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface PostTagMapper extends BaseMapper<PostTagEntity> {

    int deletePostTag(@Param("postId") String postId, @Param("tagIds") List<String> tagIds);

    int countByTagId(@Param("tagId") String tagId);

    List<String> selectTagIdByPostId(String postId);

    int insertPostTag(@Param("postId") String postId, @Param("tagIds") List<String> tagIds);
}
