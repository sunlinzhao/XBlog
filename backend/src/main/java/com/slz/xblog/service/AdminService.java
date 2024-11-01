package com.slz.xblog.service;

import com.slz.xblog.vo.UserInfoVo;

import java.util.List;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/24
 */
public interface AdminService {
    List<UserInfoVo> getAllUser();
    boolean delUser(UserInfoVo userInfoVo);

    boolean updateById(UserInfoVo userInfoVo);

    boolean addUser(UserInfoVo userInfoVo);
}
