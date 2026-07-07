# enterprise-feedback-system - 企业内部意见反馈系统

> 作者：liem

---

## 一、项目概述

本项目是一个企业内部意见反馈系统，员工可以提交意见，管理员可以查看所有意见。系统综合运用 Spring Boot、MyBatis-Plus、Thymeleaf、Redis 和 RabbitMQ 等技术构建。

### 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.18 | 基础框架 |
| MyBatis-Plus | 3.5.5 | ORM 框架 |
| Thymeleaf | 3.x | 模板引擎 |
| Redis | - | 缓存 |
| RabbitMQ | - | 消息队列 |
| MySQL | 9.4.0 | 数据库 |
| Java | 11 | 开发语言 |

---

## 二、项目结构

```
enterprise-feedback-system/
├── pom.xml                          # Maven 配置文件
├── sql/
│   └── init.sql                     # 数据库初始化脚本
├── src/
│   ├── main/
│   │   ├── java/com/liem/feedback/
│   │   │   ├── FinalExamApplication.java        # 主启动类（@EnableCaching）
│   │   │   ├── entity/
│   │   │   │   └── Feedback.java                # 意见反馈实体类
│   │   │   ├── mapper/
│   │   │   │   └── FeedbackMapper.java          # Mapper 接口（继承 BaseMapper）
│   │   │   ├── service/
│   │   │   │   ├── FeedbackService.java         # Service 接口
│   │   │   │   └── impl/
│   │   │   │       └── FeedbackServiceImpl.java # Service 实现类
│   │   │   ├── controller/
│   │   │   │   └── FeedbackController.java      # 控制器
│   │   │   ├── config/
│   │   │   │   ├── RedisConfig.java             # Redis 缓存配置
│   │   │   │   └── RabbitMQConfig.java          # RabbitMQ 配置
│   │   │   └── consumer/
│   │   │       └── FeedbackNoticeConsumer.java  # 消息消费者
│   │   └── resources/
│   │       ├── application.properties           # 应用配置
│   │       ├── templates/
│   │       │   ├── submit.html                  # 意见提交页面
│   │       │   └── list.html                    # 意见列表页面
│   │       └── static/css/
│   │           └── style.css                    # 样式表
│   └── test/
│       └── java/com/liem/feedback/
│           └── FinalExamApplicationTests.java   # 测试类
```

---

## 三、功能模块与技术点

### 5.1 数据库设计与存取（15分）

#### feedback 表结构

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键，自增 |
| username | VARCHAR(50) | 提交人姓名 |
| content | TEXT | 意见内容 |
| create_time | DATETIME | 提交时间 |

#### 关键代码

- **Feedback 实体类**：使用 `@TableName`、`@TableId`、`@TableField` 注解映射数据表
- **FeedbackMapper 接口**：继承 `BaseMapper<Feedback>`，获得单表 CRUD 能力
- **FeedbackService 实现类**：实现 `submitFeedback()` 方法保存意见到数据库

### 5.2 视图展现（15分）

#### 页面说明

- **submit.html**：使用 `th:action`、`th:object`、`th:field` 实现表单提交
- **list.html**：使用 `th:each` 遍历展示所有意见，页面底部显示作者信息
- **FeedbackController**：处理页面跳转和表单提交，提交成功后重定向到 list.html

#### UI 设计特点

- 采用现代玻璃化（Glassmorphism）设计风格
- 使用 SVG 矢量图标，不使用 emoji
- 响应式布局，适配移动端
- WCAG AA 对比度标准
- 所有页面底部显示作者信息

### 5.3 Redis 缓存存取（15分）

#### 配置要点

- 主启动类添加 `@EnableCaching` 注解
- `application.properties` 配置 `spring.cache.type=redis`
- `FeedbackService.listAll()` 方法添加 `@Cacheable(value = "feedbackList")` 注解
- 自定义 `RedisConfig`，使用 `Jackson2JsonRedisSerializer` 作为 Value 序列化器

#### 缓存效果

- 第一次访问 `/list`：查询数据库，结果写入 Redis 缓存
- 第二次访问 `/list`：直接从 Redis 读取，不执行 SQL
- Redis 中缓存键为 `feedbackList::SimpleKey []`，值为 JSON 格式

### 5.4 MQ 消息队列功能（15分）

#### 配置要点

- `RabbitMQConfig` 声明 `feedback.exchange`（Fanout 类型交换器）
- 声明 `feedback.queue` 队列并绑定到交换器
- `FeedbackService.submitFeedback()` 注入 `RabbitTemplate`，保存后发送 JSON 消息
- `FeedbackNoticeConsumer` 使用 `@RabbitListener(queues = "feedback.queue")` 监听队列

#### 消息流程

1. 用户提交意见 → 保存到数据库
2. 构造 JSON 消息（包含 username、content、createTime、studentId）
3. 通过 `RabbitTemplate.convertAndSend()` 发送到 `feedback.exchange`
4. 消费者监听 `feedback.queue`，收到消息后在控制台打印

---

## 四、运行说明

### 4.1 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- Redis
- RabbitMQ

### 4.2 数据库初始化

执行 `sql/init.sql` 脚本创建数据库和表：

```sql
CREATE DATABASE IF NOT EXISTS feedback_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE feedback_db;
CREATE TABLE feedback (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 4.3 配置修改

修改 `src/main/resources/application.properties` 中的数据库和中间件连接信息：

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/feedback_db
spring.datasource.username=root
spring.datasource.password=your_password

# Redis
spring.redis.host=localhost
spring.redis.port=6379

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### 4.4 启动项目

```bash
mvn spring-boot:run
```

或使用 IDE 运行 `FinalExamApplication.java` 主类。

### 4.5 访问地址

- 意见提交页面：http://localhost:8080/submit
- 意见列表页面：http://localhost:8080/list

---

## 五、测试数据

数据库中已插入测试数据：

| id | username | content |
|----|----------|---------|
| 1 | liem liem | 希望公司能增加下午茶供应，提升员工幸福感。 |
| 2 | liem | 建议优化内部审批流程，减少不必要的环节。 |
| 3 | liem | 办公环境整体不错，但希望会议室预约系统能更便捷。 |

---

## 六、个性化标识

1. **项目命名**：enterprise-feedback-system
2. **页面展示**：所有前端页面底部显示"作者：liem"
3. **数据库数据**：feedback 表中插入测试数据
4. **日志输出**：日志中包含作者标识（如 `liem`）
