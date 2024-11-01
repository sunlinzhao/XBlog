package com.slz.xblog.mapper;

import com.slz.xblog.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slz.xblog.vo.UserInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author slz
 * @since 2024-10-21
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    List<UserInfoVo> selectUserList();
    int updateUser(UserInfoVo userInfoVo);
    UserInfoVo selectUserByEmail(String email);

    List<UserInfoVo> getUserViewRank();
}
