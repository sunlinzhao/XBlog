package com.slz.xblog;

import com.slz.xblog.entity.AccessEntity;
import com.slz.xblog.entity.UserEntity;
import com.slz.xblog.mapper.UserMapper;
import com.slz.xblog.service.AccessService;
import com.slz.xblog.service.AdminService;
import com.slz.xblog.service.EmailService;
import com.slz.xblog.utils.GenericConverter;
import com.slz.xblog.utils.JsonUtil;
import com.slz.xblog.utils.MD5Util;
import com.slz.xblog.utils.RedisCacheUtil;
import com.slz.xblog.utils.token.JwtTokenUtil;
import com.slz.xblog.utils.generator.GeneratorBone;
import com.slz.xblog.service.IUserService;
import com.slz.xblog.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : SunLZ
 * @project : Blog
 * @date : 2024/10/21
 */

@Slf4j
@SpringBootTest
public class SpringTest {
    @Resource
    private GeneratorBone generatorBone;
    @Resource
    private IUserService userService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private EmailService emailService;
    @Resource
    private RedisCacheUtil redisCacheUtil;
    @Resource
    private UserMapper userMapper;
    @Resource
    private AdminService adminService;

    @Resource
    private AccessService accessService;

    @Test
    public void test() {
        generatorBone.run();
    }
//    @Test
//    public void test() {
//        UserInfoVo userInfoVo = new UserInfoVo().setUserName("sunlz")
//                .setRole("1")
//                .setPassword("123456")
//                .setEmail("scscs");
//        boolean b = adminService.addUser(userInfoVo);
//        userMapper.selectUserList();
//        List<UserEntity> list = userService.list();
//        log.info("ssss");
//        Object o = redisCacheUtil.get("verificationCode:sunlz99@163.com");
//        String s = JsonUtil.jsonToObject(o.toString(), String.class);
//        System.out.println(s);
//        UserInfoVo userInfoVo = new UserInfoVo();
//        userInfoVo.setEmail("sunlz99@163.com");
//        emailService.sendVerificationCode(userInfoVo);

//        System.out.println(MD5Util.md5WithSalt("sunlz99@163.com", "123456"));

//        UserEntity userEntity = new UserEntity();
////        userService.save(userEntity);
//        userEntity.setUserId("1848341888634023937");
//        UserEntity userEntity = userService.getById("1848341888634023937");
//        userEntity.setRole("sb");
//        userEntity.setEmail("2436949535@qq.com");
//        userEntity.setPassword("123456");
//        userEntity.setUserName("admin");
//        userService.updateById(userEntity);
//        String s = jwtTokenUtil.generateToken(GenericConverter.convert(userEntity, UserInfoVo.class));
//        System.out.println(s);
//        boolean b = jwtTokenUtil.validateToken(s, "1848341888634023937");
//        System.out.println(b);
//        System.out.println(jwtTokenUtil.getUserId(s));
//        System.out.println(jwtTokenUtil.getUserName(s));
//        System.out.println(jwtTokenUtil.getEmail(s));
//        System.out.println(jwtTokenUtil.getRoles(s));
//    }
}
