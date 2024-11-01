package com.slz.xblog.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.slz.xblog.utils.token.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        request.getSession().setAttribute("IP", request.getRemoteAddr());
        /*------------------------------用于权限校验---------------------------------------*/
        // 获取请求头中的 token
        String authorization = request.getHeader("Authorization");
        if (authorization == null){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.substring(7).replace("\"","");

        if(jwtTokenUtil.isTokenExpired(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置状态码为 401
//            response.getWriter().write("Token expired"); // 可选，写入提示信息
            return; // 结束请求，不再继续过滤链
        }
        if (jwtTokenUtil.validateToken(token)) {
            String userId = jwtTokenUtil.getUserId(token);
            String role = jwtTokenUtil.getRole(token);

            /*------------------------------用于访问记录---------------------------------------*/
            // http 先于 session 创建，这里设置的 session 属性，在 sessionListener create 中获取不到
            // destroy 中可以获取到
            request.getSession().setAttribute("userId", userId);
            /*------------------------------------------------------------------------------*/

            if (userId != null && role != null) {
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 设置到安全上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
