package com.slz.xblog.listener;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpSession;

public class SessionEvent extends ApplicationEvent {
    private final HttpSession session;

    public SessionEvent(Object source, HttpSession session) {
        super(source);
        this.session = session;
    }

    public HttpSession getSession() {
        return this.session;
    }
}

