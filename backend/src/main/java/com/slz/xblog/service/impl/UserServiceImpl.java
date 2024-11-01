package com.slz.xblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slz.xblog.constant.CodeConstant;
import com.slz.xblog.entity.UserEntity;
import com.slz.xblog.mapper.UserMapper;
import com.slz.xblog.service.EmailService;
import com.slz.xblog.service.AccessService;
import com.slz.xblog.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slz.xblog.utils.token.JwtTokenUtil;
import com.slz.xblog.vo.UserInfoVo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author slz
 * @since 2024-10-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private EmailService emailService;
    @Resource
    private AccessService activeService;

    // 邮箱登录
    @Override
    public UserInfoVo emailLogin(UserInfoVo user, HttpSession session) {
        UserInfoVo userInfo = userMapper.selectUserByEmail(user.getEmail());
        // 验证用户是否存在 / 密码是否正确
        if (userInfo == null || !userInfo.getPassword().equals(user.getPassword()))
            return null;
        String sessionId = session.getId();
        activeService.addUser(session);
        // 生成token
        String token = jwtTokenUtil.generateToken(userInfo, sessionId);
        userInfo.setToken(token);
        userInfo.setRole("0".equals(userInfo.getRole()) ? "admin" : "user");
        userInfo.setPassword(null);
        return userInfo;
    }

    // 邮箱注册
    @Override
    public boolean emailRegister(UserInfoVo user) {
        // 检查也验证码是否正确
        if (!emailService.verifyVerificationCode(user.getEmail(), user.getCode()))
            return false;
        // 检查用户是否存在
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        UserEntity userFind = userMapper.selectOne(wrapper);
        if (userFind != null) return false;
        // 插入新用户
        UserEntity userEntity = new UserEntity().setUserName(user.getUserName())
                .setRole(CodeConstant.ROLE_CODE_USER) // 默认普通用户
                .setPassword(user.getPassword())
                .setEmail(user.getEmail());
        return userMapper.insert(userEntity) > 0;
    }

    // 重置密码
    @Override
    public boolean resetPassword(UserInfoVo userInfoVo) {
        String email = userInfoVo.getEmail();
        String code = userInfoVo.getCode();
        String newPassword = userInfoVo.getNewPassword();
//        String pwd = MD5Util.md5WithSalt(newPassword, email); // 前端已经加密过
        boolean b = emailService.verifyVerificationCode(email, code);
        if (b) { // 验证通过
            // 先查询后更新
            UserEntity userEntity = new UserEntity().setPassword(newPassword).setEmail(email);
            UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("email", email));
            if (user != null)
                return userMapper.update(userEntity, new QueryWrapper<UserEntity>().eq("email", email)) > 0;
        }
        return false;
    }

    @Override
    @Cacheable(value = "userViewRank", key = "'userViewRank'")
    public List<UserInfoVo> getUserViewRank() {
        return userMapper.getUserViewRank();
    }
}
