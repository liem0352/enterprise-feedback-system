package com.liem.feedback.consumer;

import com.liem.feedback.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * FeedbackNoticeConsumer - 意见反馈通知消费者
 *
 * <p>使用 @RabbitListener 监听 feedback.queue 队列，</p>
 * <p>当有新意见提交时，消费消息并在控制台打印通知内容。</p>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Slf4j
@Component
public class FeedbackNoticeConsumer {

    /**
     * 消费意见反馈通知消息
     *
     * <p>监听 feedback.queue 队列，接收到消息后在控制台打印。</p>
     * <p>消息格式为 JSON，包含 username、content、createTime、studentId 字段。</p>
     *
     * @param message 接收到的 JSON 消息
     */
    @RabbitListener(queues = RabbitMQConfig.FEEDBACK_QUEUE)
    public void receiveFeedbackNotice(String message) {
        log.info("========================================");
        log.info("liem(liem) 收到意见反馈通知:");
        log.info("消息内容: {}", message);
        log.info("========================================");
        System.out.println("========================================");
        System.out.println("[liem liem] 收到意见反馈通知消息:");
        System.out.println(message);
        System.out.println("========================================");
    }
}
