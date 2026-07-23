-- ========================================
-- 企业内部意见反馈系统 - 数据库初始化脚本
-- 作者: liem
-- ========================================
-- 创建数据库
CREATE DATABASE IF NOT EXISTS feedback_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE feedback_db;
-- 删除已存在的表（如果需要重建）
DROP TABLE IF EXISTS `feedback`;
DROP TABLE IF EXISTS `sys_user`;
-- 创建 feedback 表
-- 字段: id(主键自增), username(提交人姓名), content(意见内容), create_time(提交时间)
CREATE TABLE `feedback` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
    `username`    VARCHAR(50)  NOT NULL                COMMENT '提交人姓名',
    `content`     TEXT         NOT NULL                COMMENT '意见内容',
    `create_time` DATETIME     NOT NULL                COMMENT '提交时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='意见反馈表';
-- 创建 sys_user 表（系统用户表，用于 Spring Security 认证）
-- 字段: id(主键自增), username(用户名), password(BCrypt加密密码), role(角色), enabled(是否启用)
CREATE TABLE `sys_user` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
    `username`    VARCHAR(50)  NOT NULL                COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL                COMMENT '密码（BCrypt加密存储）',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN 或 USER',
    `enabled`     TINYINT      NOT NULL DEFAULT 1      COMMENT '是否启用：1启用 0禁用',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';
-- 插入意见反馈测试数据
INSERT INTO `feedback` (`username`, `content`, `create_time`) VALUES
('liem liem', '希望公司能增加下午茶供应，提升员工幸福感。', NOW()),
('liem', '建议优化内部审批流程，减少不必要的环节。', NOW()),
('liem', '办公环境整体不错，但希望会议室预约系统能更便捷。', NOW());
-- 注意：sys_user 表的默认管理员账号由 DataInitializer 在应用启动时自动创建
-- 默认账号：admin / admin123（密码使用 BCrypt 加密存储）
-- 查询验证
SELECT * FROM `feedback`;
SELECT * FROM `sys_user`;
