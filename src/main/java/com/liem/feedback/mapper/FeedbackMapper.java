package com.liem.feedback.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liem.feedback.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * FeedbackMapper - 意见反馈数据访问接口
 *
 * <p>继承 MyBatis-Plus 的 BaseMapper&lt;Feedback&gt;，自动获得单表 CRUD 能力。</p>
 * <p>无需编写 XML 即可完成对 feedback 表的基础操作。</p>
 *
 * @author liem  liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}
