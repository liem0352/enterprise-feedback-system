package com.liem.feedback.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig - Spring Security 安全配置
 *
 * <p>配置基于 Session 的表单登录认证，保护意见列表接口。</p>
 *
 * <p>权限规则：</p>
 * <ul>
 *   <li>公开访问：/, /submit, /login, /css/**, /error</li>
 *   <li>需要 ADMIN 角色：/list</li>
 *   <li>登出：POST /logout，登出后重定向到登录页</li>
 * </ul>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 密码编码器（BCrypt 加密）
     *
     * <p>Spring Security 会在登录时使用此编码器验证密码，
     * DataInitializer 也会使用此编码器加密默认管理员密码。</p>
     *
     * @return BCrypt 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤器链配置
     *
     * <p>配置 HTTP 安全规则：表单登录、权限规则、登出处理、CSRF 防护。</p>
     *
     * @param http HttpSecurity 构建器
     * @return 配置完成的安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 授权规则配置
            .authorizeHttpRequests(auth -> auth
                // 公开访问的路径
                .antMatchers("/", "/submit", "/login", "/css/**", "/error").permitAll()
                // 意见列表需要 ADMIN 角色
                .antMatchers("/list").hasRole("ADMIN")
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            // 表单登录配置
            .formLogin(form -> form
                // 自定义登录页 URL
                .loginPage("/login")
                // 登录表单提交 URL（Spring Security 默认处理）
                .loginProcessingUrl("/login")
                // 登录成功后重定向到意见列表
                .defaultSuccessUrl("/list", true)
                // 登录失败重定向回登录页（带错误参数）
                .failureUrl("/login?error")
                // 登录页及相关资源允许所有用户访问
                .permitAll()
            )
            // 登出配置
            .logout(logout -> logout
                // 登出 URL（POST 请求）
                .logoutUrl("/logout")
                // 登出成功后重定向到登录页
                .logoutSuccessUrl("/login?logout")
                // 清除会话
                .invalidateHttpSession(true)
                // 删除认证 Cookie
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // CSRF 防护：启用（Thymeleaf 的 th:action 会自动注入 CSRF token）
            // /submit 是 POST 请求，Thymeleaf 表单已通过 th:action 自动处理 CSRF
            .csrf(csrf -> csrf);

        log.info("Spring Security 配置完成: /list 需要 ADMIN 角色, /submit 公开访问");

        return http.build();
    }
}
