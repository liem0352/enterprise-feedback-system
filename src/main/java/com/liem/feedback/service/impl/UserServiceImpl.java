package com.liem.feedback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liem.feedback.entity.User;
import com.liem.feedback.mapper.UserMapper;
import com.liem.feedback.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * UserServiceImpl - 用户业务服务实现
 *
 * <p>实现 Spring Security 的 loadUserByUsername 方法，
 * 将数据库中的 User 实体转换为 Spring Security 认证所需的 UserDetails 对象。</p>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据用户名加载用户详情（Spring Security 认证回调）
     *
     * <p>Spring Security 在登录时会调用此方法查询用户信息，
     * 然后将返回的密码与用户输入的密码进行 BCrypt 比对验证。</p>
     *
     * @param username 用户输入的用户名
     * @return Spring Security 认证用的 UserDetails 对象
     * @throws UsernameNotFoundException 当用户不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);

        if (user == null) {
            log.warn("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 检查账号是否启用
        boolean enabled = user.getEnabled() != null && user.getEnabled() == 1;

        // 构造 Spring Security UserDetails 对象
        // 角色前缀必须是 ROLE_，配合 hasRole() 使用时 Spring 会自动补全
        String role = user.getRole() != null ? user.getRole() : "USER";
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        log.info("加载用户详情成功: username={}, role={}, enabled={}", username, role, enabled);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!enabled)
                .authorities(Collections.singletonList(authority))
                .build();
    }
}
