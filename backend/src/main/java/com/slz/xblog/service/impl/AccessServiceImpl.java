package com.slz.xblog.service.impl;

import com.slz.xblog.entity.AccessEntity;
import com.slz.xblog.mapper.AccessMapper;
import com.slz.xblog.service.AccessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slz.xblog.utils.token.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 统计用户上次活跃时间 服务实现类
 * </p>
 *
 * @author slz
 * @since 2024-10-28
 */
@Service
@Slf4j
public class AccessServiceImpl extends ServiceImpl<AccessMapper, AccessEntity> implements AccessService {

    @Resource
    private AccessMapper accessMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    // 存储在线用户的Map，键为用户ID，值为最后活跃时间
//    private final Map<String, Long> onlineUsersUseHeart = new ConcurrentHashMap<>();

    // 存储在线用户的Map，键为 sessionId，值为 HttpSession
    private final Map<String, HttpSession> onlineUsers = new ConcurrentHashMap<>();



    /* --------- 心跳 实现统计 ----------*/
//    @Override
//    public void updateUserActivity(ActiveVo activeVo) {
//        onlineUsersUseHeart.put(activeVo.getUserId(), System.currentTimeMillis());
//    }


//    public Integer getOnlineCount() {
//        return onlineUsers.size();
//    }


    /* --------- HttpSession 实现统计 ----------*/
    @Override
    public void addUser(HttpSession session) {
        log.info("用户上线：{}", session.getId());
        onlineUsers.put(session.getId(), session);
    }

    @Override
    public void removeUser(String sessionId) {
        log.info("用户下线：{}", sessionId);
        HttpSession session = onlineUsers.get(sessionId);
        if(session != null)
            session.invalidate();
        onlineUsers.remove(sessionId);
    }

    @Override
    public HttpSession getSession(String sessionId) {
        return onlineUsers.get(sessionId);
    }

    @Override
    @Cacheable(value = "visitCount", key = "'visitCount'")
    public Integer getVisitCount() {
        return accessMapper.getCount();
    }

    @Override
    public void logAccess(HttpSession session) {
        String sessionId = session.getId();
        String ip = (String) session.getAttribute("IP");
        String userId = (String) session.getAttribute("userId");


        // 处理事件的逻辑
        AccessEntity record = new AccessEntity();
        record.setSessionId(sessionId);
        record.setIp(ip);
        // 判断是否游客访问
        // 这里可以根据实际情况调整
        record.setUserId(Objects.requireNonNullElse(userId, "2"));
        // 保存到数据库
        accessMapper.insert(record);
    }

//    @Scheduled(fixedRate = 60000) // 每分钟执行一次
//    public void cleanupInactiveUsers() {
//        log.info("开启定时任务：cleanupInactiveUsers");
//        long currentTime = System.currentTimeMillis();
//        onlineUsersUseHeart.entrySet().removeIf(entry ->
//                currentTime - entry.getValue() > 300000 // 5分钟未活动
//        );
}
