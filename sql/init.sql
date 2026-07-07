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

-- 创建 feedback 表
-- 字段: id(主键自增), username(提交人姓名), content(意见内容), create_time(提交时间)
CREATE TABLE `feedback` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
    `username`    VARCHAR(50)  NOT NULL                COMMENT '提交人姓名',
    `content`     TEXT         NOT NULL                COMMENT '意见内容',
    `create_time` DATETIME     NOT NULL                COMMENT '提交时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='意见反馈表';

-- 插入测试数据（包含作者信息）
INSERT INTO `feedback` (`username`, `content`, `create_time`) VALUES
('liem liem', '希望公司能增加下午茶供应，提升员工幸福感。', NOW()),
('liem', '建议优化内部审批流程，减少不必要的环节。', NOW()),
('liem', '办公环境整体不错，但希望会议室预约系统能更便捷。', NOW());

-- 查询验证
SELECT * FROM `feedback`;
