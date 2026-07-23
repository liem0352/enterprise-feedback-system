package com.liem.feedback.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User - 系统用户实体类
 *
 * <p>对应数据库 sys_user 表，用于 Spring Security 认证。</p>
 * <p>密码字段存储 BCrypt 加密后的密文，不存储明文。</p>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User {

    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 密码（BCrypt 加密存储）
     */
    private String password;

    /**
     * 角色：ADMIN 或 USER
     */
    private String role;

    /**
     * 是否启用：1启用 0禁用
     */
    private Integer enabled;
}
