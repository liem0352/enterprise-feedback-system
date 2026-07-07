package com.liem.feedback.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Objects;

/**
 * RedisConfig - Redis 缓存自定义配置
 *
 * <p>使用 Jackson2JsonRedisSerializer 作为 Value 的序列化器，</p>
 * <p>使得存储在 Redis 中的数据为可读的 JSON 格式。</p>
 *
 * <p>配置内容：</p>
 * <ul>
 *   <li>RedisTemplate: Key 使用 String 序列化，Value 使用 JSON 序列化</li>
 *   <li>RedisCacheManager: 缓存配置，默认 TTL 60 分钟，JSON 序列化</li>
 * </ul>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 配置 RedisTemplate
     *
     * <p>Key 使用 StringRedisSerializer，Value 使用 Jackson2JsonRedisSerializer。</p>
     * <p>这样 Redis 中存储的数据为可读的 JSON 格式。</p>
     *
     * @param connectionFactory Redis 连接工厂
     * @return RedisTemplate 实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 显式断言非空，消除 Spring @NonNull 注解的类型安全警告
        template.setConnectionFactory(Objects.requireNonNull(connectionFactory));

        // 配置 ObjectMapper 支持 Java 8 时间类型
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.registerModule(new JavaTimeModule());

        // 使用 Jackson2JsonRedisSerializer 作为 Value 序列化器
        Jackson2JsonRedisSerializer<Object> jsonSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        jsonSerializer.setObjectMapper(objectMapper);

        // Key 使用 String 序列化
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置 RedisCacheManager
     *
     * <p>缓存 Key 使用 String 序列化，Value 使用 GenericJackson2JsonRedisSerializer。</p>
     * <p>默认缓存过期时间 60 分钟。</p>
     *
     * @param connectionFactory Redis 连接工厂
     * @return RedisCacheManager 实例
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 配置 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.registerModule(new JavaTimeModule());

        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Objects.requireNonNull(Duration.ofMinutes(60)))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(Objects.requireNonNull(connectionFactory))
                .cacheDefaults(config)
                .build();
    }
}
