package com.liem.feedback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liem.feedback.entity.Feedback;
import com.liem.feedback.mapper.FeedbackMapper;
import com.liem.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FeedbackServiceImpl - 意见反馈业务实现类
 *
 * <p>实现意见提交与查询的核心业务逻辑：</p>
 * <ul>
 *   <li>submitFeedback: 保存意见到数据库，并通过 RabbitMQ 发送 JSON 通知消息</li>
 *   <li>listAll: 查询全部意见，使用 @Cacheable 实现 Redis 缓存</li>
 * </ul>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    /**
     * RabbitMQ 交换器名称
     */
    public static final String FEEDBACK_EXCHANGE = "feedback.exchange";

    /**
     * RabbitTemplate 用于向交换器发送消息（通过构造器注入）
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Jackson ObjectMapper 用于将消息序列化为 JSON
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 提交意见反馈
     *
     * <p>业务流程：</p>
     * <ol>
     *   <li>设置提交时间为当前时间</li>
     *   <li>调用 MyBatis-Plus save 方法写入数据库</li>
     *   <li>通过 @CacheEvict 清除 feedbackList 缓存，保证列表页能读到最新数据</li>
     *   <li>构造包含用户名和意见内容的 JSON 消息</li>
     *   <li>通过 RabbitTemplate 发送到 feedback.exchange 交换器</li>
     * </ol>
     *
     * @param feedback 意见反馈对象
     * @return 是否提交成功
     */
    @Override
    @CacheEvict(value = "feedbackList", allEntries = true)
    public boolean submitFeedback(Feedback feedback) {
        // 设置提交时间
        feedback.setCreateTime(LocalDateTime.now());

        // 保存到数据库
        boolean saved = save(feedback);
        log.info("liem(liem) 提交了意见: username={}, content={}",
                feedback.getUsername(), feedback.getContent());

        if (saved) {
            // 构造 JSON 格式通知消息
            try {
                Map<String, Object> message = new HashMap<>(4);
                message.put("username", feedback.getUsername());
                message.put("content", feedback.getContent());
                message.put("createTime", feedback.getCreateTime().toString());
                message.put("studentId", "liem");

                String jsonMessage = objectMapper.writeValueAsString(message);

                // 发送消息到 feedback.exchange 交换器（Fanout 类型不需要 routing key）
                rabbitTemplate.convertAndSend(FEEDBACK_EXCHANGE, "", jsonMessage);
                log.info("liem 已发送 MQ 通知消息: {}", jsonMessage);
            } catch (JsonProcessingException e) {
                log.error("消息序列化失败 - liem(liem)", e);
            }
        }
        return saved;
    }

    /**
     * 查询全部意见反馈（带 Redis 缓存）
     *
     * <p>使用 @Cacheable 注解，缓存名为 feedbackList。</p>
     * <p>第一次访问时查询数据库并写入 Redis；第二次访问直接从 Redis 读取。</p>
     * <p>按提交时间降序排列（最新意见在前）。</p>
     *
     * @return 意见列表
     */
    @Override
    @Cacheable(value = "feedbackList")
    public List<Feedback> listAll() {
        log.info("liem(liem) 正在从数据库查询意见列表...");
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }
}
