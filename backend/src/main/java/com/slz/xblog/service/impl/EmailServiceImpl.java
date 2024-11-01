package com.slz.xblog.service.impl;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/23
 */
import com.slz.xblog.service.EmailService;
import com.slz.xblog.utils.JsonUtil;
import com.slz.xblog.utils.RedisCacheUtil;
import com.slz.xblog.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Resource
    private RedisCacheUtil redisCacheUtil;

    // 发送验证码
    @Override
    public boolean sendVerificationCode(UserInfoVo userInfoVo) {
        String email = userInfoVo.getEmail();
        if (email==null) return false;
        String verificationCode = generateVerificationCode();
        String subject = "XBlog 邮箱验证码";
        String body = "您的验证码是: " + verificationCode + "，有效时间 5 分钟。";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(senderEmail); // 替换为你的发件邮箱

        mailSender.send(message);
        log.info("验证码已发送到 " + email);

        // 验证码存入缓存，并设置过期时间
        redisCacheUtil.setWithExpire("verificationCode:" + email, verificationCode, 60*5);
        return true; // 可以返回验证码以便后续验证
    }

    // 生成验证码
    @Override
    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 生成 6 位随机验证码
    }


    // 验证验证码
    @Override
    public boolean verifyVerificationCode(String email, String verificationCode) {
        if (redisCacheUtil.exists("verificationCode:" + email)) {
            Object o = redisCacheUtil.get("verificationCode:" + email);
            String storedCode = JsonUtil.jsonToObject(o.toString(), String.class);

            // 如果验证通过，删除缓存并返回 true
            if (storedCode.equals(verificationCode)) {
                redisCacheUtil.delete("verificationCode:" + email);
                return true;
            }
        }
        return false;
    }

}

