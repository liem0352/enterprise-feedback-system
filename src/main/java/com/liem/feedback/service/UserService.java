package com.liem.feedback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liem.feedback.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService - 用户业务服务接口
 *
 * <p>同时继承 MyBatis-Plus 的 IService 和 Spring Security 的 UserDetailsService，
 * 既能复用通用 Service 能力，又能提供认证所需的用户查询功能。</p>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
public interface UserService extends IService<User>, UserDetailsService {
}
