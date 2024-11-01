package com.slz.xblog.mapper;

import com.slz.xblog.entity.AccessEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 统计用户上次活跃时间 Mapper 接口
 * </p>
 *
 * @author slz
 * @since 2024-10-28
 */
@Mapper
public interface AccessMapper extends BaseMapper<AccessEntity> {

    Integer getCount();
}
