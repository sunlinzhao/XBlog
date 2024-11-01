package com.slz.xblog.listener;

import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CustomSessionListener implements HttpSessionListener {
    @Resource
    private ApplicationEventPublisher eventPublisher;

    // HTTP 请求总是先于 HTTPSession 的创建或使用发生。
    // 当第一次请求需要维护用户状态时，会创建一个新的会话；对于后续请求，只要会话还在有效期内，就会复用同一个会话。
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // 每次创建会话时增加计数
        ServletContext context = event.getSession().getServletContext();
        Integer count = (Integer) context.getAttribute("activeSessionCount");
        if (count == null) {
            count = 0;
        }
        context.setAttribute("activeSessionCount", count + 1);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // 每次销毁会话时减少计数
        ServletContext context = event.getSession().getServletContext();
        Integer count = (Integer) context.getAttribute("activeSessionCount");
        if (count != null && count > 0) {
            context.setAttribute("activeSessionCount", count - 1);
        }
        // http 先于 session 创建，这里设置的 session 属性，在 sessionCreated 中获取不到
        // 所以这里放在sessionDestroyed 中
        // 发布会话摧毁事件
        eventPublisher.publishEvent(new SessionEvent(this, event.getSession()));
    }

}
