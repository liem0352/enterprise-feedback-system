package com.liem.feedback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * FinalExamApplication - 企业内部意见反馈系统主启动类
 * 作者: liem
 * 姓名: liem
 *
 * <p>使用 @EnableCaching 开启 Spring Cache 支持，配合 Redis 实现意见列表缓存。</p>
 * <p>使用 @MapperScan 自动扫描 MyBatis-Plus Mapper 接口。</p>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@SpringBootApplication
@EnableCaching
@MapperScan("com.liem.feedback.mapper")
public class FinalExamApplication {

    /**
     * 应用程序入口方法
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(FinalExamApplication.class, args);
        System.out.println("========================================");
        System.out.println("作者: liem");
        System.out.println("企业内部意见反馈系统 启动成功!");
        System.out.println("========================================");
    }
}
