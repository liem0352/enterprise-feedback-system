<p align="center">
  <img src="./assets/readme/hero.svg" width="100%" alt="enterprise-feedback-system 企业内部意见反馈系统,基于 SpringBoot + Redis + RabbitMQ">
</p>

# 企业内部意见反馈系统

一个让员工提交意见、让管理员查看全部意见的企业内部反馈系统。意见先写入 MySQL，再由 RabbitMQ 异步派发到通知消费者；列表查询走 Redis 缓存，第二次访问直接命中缓存、不再查库。

## 核心机制

一次意见提交会同时触发三条链路：

- **持久化**：意见写入 MySQL `feedback` 表，MyBatis-Plus 单表 CRUD
- **缓存**：`/list` 接口标注 `@Cacheable("feedbackList")`，首次查库后写入 Redis，后续直接命中
- **消息**：保存后通过 `RabbitTemplate` 把 JSON 消息发往 `feedback.exchange`（Fanout），消费者 `@RabbitListener` 监听 `feedback.queue` 在控制台打印通知

## 功能与技术点

| 模块 | 实现要点 |
|------|----------|
| 数据存取 | `Feedback` 实体用 `@TableName/@TableId/@TableField` 映射；`FeedbackMapper` 继承 `BaseMapper` |
| 视图层 | Thymeleaf `th:action/th:object/th:field` 表单提交；`th:each` 遍历列表；玻璃化 UI、SVG 图标、响应式 |
| Redis 缓存 | 主启动类 `@EnableCaching`；`RedisConfig` 用 `Jackson2JsonRedisSerializer`；缓存键 `feedbackList::SimpleKey []` |
| RabbitMQ | `RabbitMQConfig` 声明 Fanout 交换器 + 队列绑定；`FeedbackNoticeConsumer` 监听消费 |

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.18 | 基础框架 |
| MyBatis-Plus | 3.5.5 | ORM |
| Thymeleaf | 3.x | 模板引擎 |
| Redis | - | 缓存 |
| RabbitMQ | - | 消息队列 |
| MySQL | 9.4.0 | 数据库 |
| Java | 11 | 开发语言 |

## 项目结构

```
enterprise-feedback-system/
├── pom.xml
├── sql/init.sql
└── src/main/
    ├── java/com/liem/feedback/
    │   ├── FinalExamApplication.java      # @EnableCaching 主启动类
    │   ├── entity/Feedback.java
    │   ├── mapper/FeedbackMapper.java      # BaseMapper<Feedback>
    │   ├── service/{FeedbackService, impl/FeedbackServiceImpl}
    │   ├── controller/FeedbackController.java
    │   ├── config/{RedisConfig, RabbitMQConfig}
    │   └── consumer/FeedbackNoticeConsumer.java
    └── resources/
        ├── application.properties
        ├── templates/{submit.html, list.html}
        └── static/css/style.css
```

## 快速开始

**环境**：JDK 11+ · Maven 3.6+ · MySQL 8.0+ · Redis · RabbitMQ

1. 初始化数据库
   ```sql
   CREATE DATABASE feedback_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   USE feedback_db;
   CREATE TABLE feedback (
       id INT NOT NULL AUTO_INCREMENT,
       username VARCHAR(50) NOT NULL,
       content TEXT NOT NULL,
       create_time DATETIME NOT NULL,
       PRIMARY KEY (id)
   ) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE=utf8mb4_unicode_ci;
   ```
2. 修改 `src/main/resources/application.properties` 中的 MySQL / Redis / RabbitMQ 连接信息
3. 启动
   ```bash
   mvn spring-boot:run
   ```
4. 访问
   - 意见提交：http://localhost:8080/submit
   - 意见列表：http://localhost:8080/list

## 缓存与消息验证

- 第一次访问 `/list`：查询数据库，结果写入 Redis（键 `feedbackList::SimpleKey []`）
- 第二次访问 `/list`：直接从 Redis 读取，不执行 SQL
- 提交意见后：控制台输出消费者收到的消息（含 username、content、createTime）

## 测试数据

| id | username | content |
|----|----------|---------|
| 1 | liem liem | 希望公司能增加下午茶供应，提升员工幸福感。 |
| 2 | liem | 建议优化内部审批流程，减少不必要的环节。 |
| 3 | liem | 办公环境整体不错，但希望会议室预约系统能更便捷。 |

## 个性化标识

- 项目命名：`enterprise-feedback-system`
- 所有前端页面底部显示「作者：liem」
- 数据库与日志中包含作者标识 `liem`

---

<p align="center"><sub>作者 liem · 企业内部意见反馈系统</sub></p>
