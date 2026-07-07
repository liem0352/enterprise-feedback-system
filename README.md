# enterprise-feedback-system - 

> liem

---

## Spring BootMyBatis-PlusThymeleafRedis  RabbitMQ 

### |  |  |  |
|------|------|------|
| Spring Boot | 2.7.18 |  |
| MyBatis-Plus | 3.5.5 | ORM  |
| Thymeleaf | 3.x |  |
| Redis | - |  |
| RabbitMQ | - |  |
| MySQL | 9.4.0 |  |
| Java | 11 |  |

---

## ```
enterprise-feedback-system/
 pom.xml                          # Maven 
 sql/
    init.sql                     # 
 src/
    main/
       java/com/liem/feedback/
          FinalExamApplication.java        # @EnableCaching
          entity/
             Feedback.java                # 
          mapper/
             FeedbackMapper.java          # Mapper  BaseMapper
          service/
             FeedbackService.java         # Service 
             impl/
                 FeedbackServiceImpl.java # Service 
          controller/
             FeedbackController.java      # 
          config/
             RedisConfig.java             # Redis 
             RabbitMQConfig.java          # RabbitMQ 
          consumer/
              FeedbackNoticeConsumer.java  # 
       resources/
           application.properties           # 
           templates/
              submit.html                  # 
              list.html                    # 
           static/css/
               style.css                    # 
    test/
        java/com/liem/feedback/
            FinalExamApplicationTests.java   # 
```

---

## ### 5.1 15

#### feedback 

|  |  |  |
|------|------|------|
| id | INT |  |
| username | VARCHAR(50) |  |
| content | TEXT |  |
| create_time | DATETIME |  |

#### - **Feedback ** `@TableName``@TableId``@TableField` 
- **FeedbackMapper ** `BaseMapper<Feedback>` CRUD 
- **FeedbackService ** `submitFeedback()` 

### 5.2 15

#### - **submit.html** `th:action``th:object``th:field` 
- **list.html** `th:each` 
- **FeedbackController** list.html

#### UI 

- Glassmorphism
-  SVG  emoji
- 
- WCAG AA 
- 

### 5.3 Redis 15

#### -  `@EnableCaching` 
- `application.properties`  `spring.cache.type=redis`
- `FeedbackService.listAll()`  `@Cacheable(value = "feedbackList")` 
-  `RedisConfig` `Jackson2JsonRedisSerializer`  Value 

#### -  `/list` Redis 
-  `/list` Redis  SQL
- Redis  `feedbackList::SimpleKey []` JSON 

### 5.4 MQ 15

#### - `RabbitMQConfig`  `feedback.exchange`Fanout 
-  `feedback.queue` 
- `FeedbackService.submitFeedback()`  `RabbitTemplate` JSON 
- `FeedbackNoticeConsumer`  `@RabbitListener(queues = "feedback.queue")` 

#### 1.  → 
2.  JSON  usernamecontentcreateTimestudentId
3.  `RabbitTemplate.convertAndSend()`  `feedback.exchange`
4.  `feedback.queue`

---

## ### 4.1 

- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- Redis
- RabbitMQ

### 4.2 

 `sql/init.sql` 

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

### 4.3 

 `src/main/resources/application.properties` 

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

### 4.4 

```bash
mvn spring-boot:run
```

 IDE  `FinalExamApplication.java` 

### 4.5 

- http://localhost:8080/submit
- http://localhost:8080/list

---

## | id | username | content |
|----|----------|---------|
| 1 | liem liem |  |
| 2 | liem |  |
| 3 | liem |  |

---

## 1. ****enterprise-feedback-system
2. ****"liem"
3. ****feedback 
4. **** `liem`
