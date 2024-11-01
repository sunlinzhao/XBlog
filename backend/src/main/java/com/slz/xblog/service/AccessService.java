package com.slz.xblog.service;

import com.slz.xblog.entity.AccessEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 统计用户上次活跃时间 服务类
 * </p>
 *
 * @author slz
 * @since 2024-10-28
 */
public interface AccessService extends IService<AccessEntity> {
//    void updateUserActivity(ActiveVo activeVo);
    //    void cleanupInactiveUsers();

    void removeUser(String sessionId);
    void addUser(HttpSession session);

//    Integer getOnlineCount();

    HttpSession getSession(String sessionId);

    Integer getVisitCount();

    void logAccess(HttpSession session);
}
