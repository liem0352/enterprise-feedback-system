package com.liem.feedback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * FinalExamApplicationTests - 应用上下文加载测试
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@SpringBootTest
class FinalExamApplicationTests {

    /**
     * 测试 Spring 上下文能否正常加载
     */
    @Test
    void contextLoads() {
        System.out.println("作者: liem - 上下文加载测试通过");
    }
}
