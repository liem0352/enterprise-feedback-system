package com.liem.feedback.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Feedback - 意见反馈实体类
 *
 * <p>对应数据库 feedback 表，使用 MyBatis-Plus 注解完成 ORM 映射。</p>
 * <p>字段说明：</p>
 * <ul>
 *   <li>id - 主键，自增</li>
 *   <li>username - 提交人姓名</li>
 *   <li>content - 意见内容</li>
 *   <li>createTime - 提交时间</li>
 * </ul>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Data
@TableName("feedback")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID，数据库自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提交人姓名
     */
    @TableField("username")
    private String username;

    /**
     * 意见内容
     */
    @TableField("content")
    private String content;

    /**
     * 提交时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 无参构造方法
     */
    public Feedback() {
    }

    /**
     * 全参构造方法
     *
     * @param id         主键 ID
     * @param username   提交人姓名
     * @param content    意见内容
     * @param createTime 提交时间
     */
    /*
    liem liem
     */
    public Feedback(Long id, String username, String content, LocalDateTime createTime) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.createTime = createTime;
    }
}
