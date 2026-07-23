package com.liem.feedback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * AuthController - 认证控制器
 *
 * <p>负责提供自定义登录页面。</p>
 *
 * <p>注意：登录表单的提交处理（POST /login）由 Spring Security 自动处理，
 * 不需要在此控制器中编写处理逻辑。</p>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Slf4j
@Controller
public class AuthController {

    /**
     * 显示登录页面
     *
     * <p>GET /login 返回 login.html 视图。</p>
     * <p>支持以下查询参数：</p>
     * <ul>
     *   <li>?error - 登录失败，页面显示错误提示</li>
     *   <li>?logout - 登出成功，页面显示登出提示</li>
     * </ul>
     *
     * @return 视图名称 login
     */
    @GetMapping("/login")
    public String loginPage() {
        log.info("访问登录页面");
        return "login";
    }
}
