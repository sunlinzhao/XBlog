package com.slz.xblog.mapper;

import com.slz.xblog.entity.TagEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slz.xblog.vo.TagVo;
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
public interface TagMapper extends BaseMapper<TagEntity> {

    String selectByTagName(String tagName);

    int deleteTags(@Param("tagIdList") List<String> tagIdList);

    List<String> getAllTags();

    List<TagVo> getTagPostsNum();
}
