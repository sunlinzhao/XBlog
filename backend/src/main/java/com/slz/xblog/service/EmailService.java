package com.slz.xblog.service;

import com.slz.xblog.vo.UserInfoVo;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/23
 */
public interface EmailService {
    boolean sendVerificationCode(UserInfoVo u4);
    String generateVerificationCode();
    boolean verifyVerificationCode(String email, String verificationCode);
}
