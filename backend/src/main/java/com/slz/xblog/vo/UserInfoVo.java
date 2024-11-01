package com.slz.xblog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userName;
    private String email;
    private String role;
    private String token;
    private String code; // 验证码
    private String password;
    private String newPassword; // 新密码
    private String viewCount; // 用户文章浏览量
    private String roleDisplay;

    public String getRoleDisplay() {
        return "0".equals(role) ? "admin" : "user";
    }
}
