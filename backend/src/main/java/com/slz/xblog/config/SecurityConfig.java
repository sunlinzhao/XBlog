package com.slz.xblog.config;

import com.slz.xblog.filter.JwtAuthenticationFilter;
import com.slz.xblog.filter.JwtTokenFilter;
import com.slz.xblog.utils.token.JwtTokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Resource
    private JwtTokenFilter jwtTokenFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 根据需要选择是否禁用 CSRF 跨站请求伪造
                .authorizeRequests() // 配置请求授权规则
                .antMatchers("/swagger-ui/**","/v3/api-docs/**", "/swagger-resources/**").permitAll() // 允许访问 Swagger UI
                .antMatchers("/xblog/public/**").permitAll() // 允许访问公共接口
                .antMatchers("/xblog/admin/**").hasRole("admin") // 允许访问管理员接口
                .antMatchers("/xblog/user/**").hasAnyRole("user", "admin") // 允许访问用户接口
                .anyRequest().authenticated() // 其他请求需要认证
                .and()
//                .httpBasic() // 使用 HTTP Basic 认证
//                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 添加自定义的 JWT 认证过滤器
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // 将过滤器添加到安全过滤链中
                .cors() // 启用跨域资源共享
        ;
    }

//    // 将一些临时用户角色存在内存中，开发时使用
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user@user").password(passwordEncoder().encode("user")).roles("user")
//                .and()
//                .withUser("admin@admin").password(passwordEncoder().encode("admin")).roles("admin");
//    }
//    // 密码加密
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
// 下面是使用临时用户角色
//    @RestController
//    @RequestMapping("/login")
//    public class AuthController {
//
//        @Autowired
//        private AuthenticationManager authenticationManager;
//
//        @PostMapping
//        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//            try {
//                Authentication authentication = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//                );
//                // 认证成功，生成 token 或返回用户信息
//                return ResponseEntity.ok("登录成功");
//            } catch (BadCredentialsException e) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
//            }
//        }
//    }
}
