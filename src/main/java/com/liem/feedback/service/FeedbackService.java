package com.liem.feedback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liem.feedback.entity.Feedback;

import java.util.List;

/**
 * FeedbackService - 意见反馈业务接口
 *
 * <p>继承 MyBatis-Plus 的 IService&lt;Feedback&gt;，获得通用 Service 能力。</p>
 * <p>自定义方法：</p>
 * <ul>
 *   <li>submitFeedback - 提交意见（保存数据库 + 发送 MQ 通知）</li>
 *   <li>listAll - 查询全部意见（带 Redis 缓存）</li>
 * </ul>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
public interface FeedbackService extends IService<Feedback> {

    /**
     * 提交意见反馈
     *
     * <p>流程：保存到数据库 -> 通过 RabbitMQ 发送通知消息。</p>
     *
     * @param feedback 意见反馈对象
     * @return 是否提交成功
     */
    boolean submitFeedback(Feedback feedback);

    /**
     * 查询全部意见反馈（带 Redis 缓存）
     *
     * <p>使用 @Cacheable 注解，缓存 key 为 feedbackList::SimpleKey []。</p>
     * <p>第二次访问时直接从 Redis 读取，不再查询数据库。</p>
     *
     * @return 意见列表
     */
    List<Feedback> listAll();
}
