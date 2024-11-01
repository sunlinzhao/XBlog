package com.slz.xblog.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.slz.xblog.utils.token.JwtTokenUtil;
import com.slz.xblog.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtTokenFilter implements javax.servlet.Filter {
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 获取请求头中的 Authorization 字段
        String authorization = httpRequest.getHeader("Authorization");
        if (authorization == null){
            chain.doFilter(request, response);
            return;
        }
        String token = authorization.substring(7).replace("\"","");

        // 生成新的 token（可以根据需要从请求中获取用户信息）
        String newToken = jwtTokenUtil.refreshToken(token);

        // 将新的 token 添加到响应头中
        ((HttpServletResponse) response).setHeader("Authorization", "Bearer " + newToken);

        // 继续过滤器链
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码（如果需要）
    }

    @Override
    public void destroy() {
        // 清理代码（如果需要）
    }
}
