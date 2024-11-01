package com.slz.xblog.utils.token;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.slz.xblog.vo.UserInfoVo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenUtil {
    @Value("${jwt.secretKey}")
    private String SECRET_KEY; // 替换为你自己的密钥
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME; // 有效时间 / 毫秒
    @Value("${jwt.token.name}")
    private String TOKEN_NAME;

    // 从请求头中获取 token
    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_NAME.equals(cookie.getName())) {
                    return cookie.getValue(); // 返回 token
                }
            }
        }
        return null; // 没有找到 token
    }

    // 生成 token
    public String generateToken(UserInfoVo user, String sessionId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user, sessionId);
    }

    private String createToken(Map<String, Object> claims, UserInfoVo user, String sessionId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(user.getUserId())
                .withClaim("userName", user.getUserName())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRoleDisplay())
                .withClaim("sessionId", sessionId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // 验证 token
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build();
            verifier.verify(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // 刷新 token
    public String refreshToken(String token) {
            // 验证 token 是否有效
            if (!validateToken(token)) {
                return null;
            }
            UserInfoVo userInfo = getUserInfo(token);
            // 生成新的 token
            return generateToken(userInfo, getSessionId(token));
    }

    // 从 token 中提取用户 ID
    public String getUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 从 token 中提取角色
    public String getRole(String token) {
        return extractAllClaims(token).getClaim("role").asString();
    }

    // 从 token 中提取用户名
    public String getUserName(String token) {
        return extractAllClaims(token).getClaim("userName").asString();
    }

    // 从 token 中提取邮箱
    public String getEmail(String token) {
        return extractAllClaims(token).getClaim("email").asString();
    }

    public String getSessionId(String token) {
        return extractAllClaims(token).getClaim("sessionId").asString();
    }

    // 检查 token 是否过期
    public boolean isTokenExpired(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.error("无效的令牌：为空或格式不正确");
            return true; // 返回 true，表示令牌无效
        }
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            // 检查 Token 是否过期
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            log.error("token 无效或过期：{}", token);
            return true; // 令牌无效，返回 true
        }
    }


    // 获取用户信息
    private UserInfoVo getUserInfo(String token) {
        DecodedJWT jwt = extractAllClaims(token);
        String userId = jwt.getSubject();
        String userName = jwt.getClaim("userName").asString();
        String email = jwt.getClaim("email").asString();
        String role = jwt.getClaim("role").asString();

        return new UserInfoVo().setUserId(userId)
                .setRole(role).setEmail(email).setUserName(userName);
    }

    // 提取所有声明
    private DecodedJWT extractAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.error("无效的令牌：为空或格式不正确");
            throw new IllegalArgumentException("无效的令牌");
        }
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            return verifier.verify(token);
        } catch (Exception e) {
            log.error("解码 JWT 失败，令牌格式可能不正确：{}", token);
            throw new TokenExpiredException("token 无效");
        }
    }

    // 从 JWT 或其他类型的令牌中解析出用户的身份信息，并返回一个 Authentication 对象
    public Authentication getAuthentication(String token) {
        // 解析令牌并获取用户信息
        DecodedJWT decodedJWT = extractAllClaims(token); // 提取所有声明

        // 获取用户 ID
        Claim userIdClaim = decodedJWT.getClaim("userId");
        String userId = userIdClaim.asString();

        // 获取角色
        Claim roleClaim = decodedJWT.getClaim("role");
        List<SimpleGrantedAuthority> authorities;

        if (roleClaim.isNull()) {
            authorities = Collections.emptyList(); // 如果没有角色声明，返回空列表
        } else if (roleClaim.asList(String.class) != null) {
            // 如果角色是一个列表
            authorities = roleClaim.asList(String.class)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            // 如果角色是一个单个字符串
            authorities = Collections.singletonList(new SimpleGrantedAuthority(roleClaim.asString()));
        }

        // 创建 Authentication 对象
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }

}
