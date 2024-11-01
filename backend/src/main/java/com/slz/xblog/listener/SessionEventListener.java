package com.slz.xblog.listener;
import com.slz.xblog.service.AccessService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SessionEventListener {
    @Resource
    private AccessService accessService;

    @EventListener
    public void handleSessionCreatedEvent(SessionEvent event) {
        // 记录访问信息
        accessService.logAccess(event.getSession());
    }
}
