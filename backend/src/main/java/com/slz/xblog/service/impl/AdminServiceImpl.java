package com.slz.xblog.service.impl;

import com.slz.xblog.entity.UserEntity;
import com.slz.xblog.mapper.UserMapper;
import com.slz.xblog.service.AdminService;
import com.slz.xblog.utils.GenericConverter;
import com.slz.xblog.utils.JsonUtil;
import com.slz.xblog.utils.MD5Util;
import com.slz.xblog.vo.UserInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/24
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserInfoVo> getAllUser() {
        return userMapper.selectUserList();
    }

    @Override
    public boolean delUser(UserInfoVo userInfoVo) {
        return userMapper.deleteById(userInfoVo.getUserId()) > 0;
    }

    @Override
    public boolean updateById(UserInfoVo userInfoVo) {
        if(userMapper.updateUser(userInfoVo) > 0) return true;
        return false;
    }
    @Override
    public boolean addUser(UserInfoVo userInfoVo) {
        String password = userInfoVo.getPassword();
        String pwd = MD5Util.md5WithSalt(password, userInfoVo.getEmail());
        userInfoVo.setPassword(pwd).setRole("1");
        UserEntity convert = GenericConverter.convert(userInfoVo, UserEntity.class);
        if(userMapper.insert(convert) > 0) return true;
        return false;
    }
}
