package com.slz.xblog.service;

import com.slz.xblog.entity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slz.xblog.vo.UserInfoVo;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author slz
 * @since 2024-10-21
 */
public interface IUserService extends IService<UserEntity> {
    UserInfoVo emailLogin(UserInfoVo user, HttpSession session);
    boolean emailRegister(UserInfoVo user);

    boolean resetPassword(UserInfoVo userInfoVo);

    List<UserInfoVo> getUserViewRank();
}
