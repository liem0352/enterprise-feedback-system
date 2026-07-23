package com.liem.feedback.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liem.feedback.entity.User;
import com.liem.feedback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - 数据初始化器
 *
 * <p>应用启动时自动检查并创建默认管理员账号。</p>
 *
 * <p>默认账号信息：</p>
 * <ul>
 *   <li>用户名：admin</li>
 *   <li>密码：admin123（BCrypt 加密存储）</li>
 *   <li>角色：ADMIN</li>
 * </ul>
 *
 * <p>注意：生产环境请在首次登录后立即修改默认密码。</p>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    /**
     * 默认管理员用户名
     */
    private static final String DEFAULT_ADMIN_USERNAME = "admin";

    /**
     * 默认管理员密码（明文，将被 BCrypt 加密后存储）
     */
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    /**
     * 默认管理员角色
     */
    private static final String DEFAULT_ADMIN_ROLE = "ADMIN";

    /**
     * 用户业务服务
     */
    private final UserService userService;

    /**
     * 密码编码器（BCrypt）
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 应用启动时执行
     *
     * <p>检查 admin 账号是否存在，不存在则创建。</p>
     *
     * @param args 启动参数
     */
    @Override
    public void run(String... args) {
        // 检查默认管理员是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, DEFAULT_ADMIN_USERNAME);
        User existingAdmin = userService.getOne(wrapper);

        if (existingAdmin == null) {
            // 创建默认管理员账号
            User admin = User.builder()
                    .username(DEFAULT_ADMIN_USERNAME)
                    .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                    .role(DEFAULT_ADMIN_ROLE)
                    .enabled(1)
                    .build();
            userService.save(admin);
            log.info("========================================");
            log.info("默认管理员账号创建成功");
            log.info("用户名: {}", DEFAULT_ADMIN_USERNAME);
            log.info("密码: {} (请及时修改)", DEFAULT_ADMIN_PASSWORD);
            log.info("========================================");
        } else {
            log.info("默认管理员账号已存在，跳过创建");
        }
    }
}
