package com.liem.feedback.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQConfig - RabbitMQ 消息队列配置
 *
 * <p>声明 Fanout 类型交换器 feedback.exchange 和队列 feedback.queue，</p>
 * <p>并将它们绑定。Fanout 交换器会将消息广播到所有绑定的队列。</p>
 *
 * <p>配置内容：</p>
 * <ul>
 *   <li>feedback.exchange - Fanout 类型交换器</li>
 *   <li>feedback.queue - 持久化队列</li>
 *   <li>Binding - 将队列绑定到交换器</li>
 * </ul>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 交换器名称
     */
    public static final String FEEDBACK_EXCHANGE = "feedback.exchange";

    /**
     * 队列名称
     */
    public static final String FEEDBACK_QUEUE = "feedback.queue";

    /**
     * 声明 Fanout 类型交换器
     *
     * <p>Fanout 交换器会忽略 routing key，将消息广播到所有绑定的队列。</p>
     *
     * @return FanoutExchange 实例
     */
    @Bean
    public FanoutExchange feedbackExchange() {
        return new FanoutExchange(FEEDBACK_EXCHANGE, true, false);
    }

    /**
     * 声明持久化队列
     *
     * <p>队列名为 feedback.queue，durable=true 表示队列持久化。</p>
     *
     * @return Queue 实例
     */
    @Bean
    public Queue feedbackQueue() {
        return new Queue(FEEDBACK_QUEUE, true);
    }

    /**
     * 将队列绑定到 Fanout 交换器
     *
     * <p>Fanout 交换器不需要 routing key，直接绑定即可。</p>
     *
     * @param queue    队列
     * @param exchange 交换器
     * @return Binding 绑定关系
     */
    @Bean
    public Binding bindingFeedback(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
