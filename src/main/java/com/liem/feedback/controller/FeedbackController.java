package com.liem.feedback.controller;

import com.liem.feedback.entity.Feedback;
import com.liem.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * FeedbackController - 意见反馈控制器
 *
 * <p>负责页面跳转和表单提交处理：</p>
 * <ul>
 *   <li>GET / - 重定向到提交页面</li>
 *   <li>GET /submit - 显示意见提交页面</li>
 *   <li>POST /submit - 处理表单提交，成功后重定向到列表页</li>
 *   <li>GET /list - 显示意见列表页面</li>
 * </ul>
 *
 * @author liem
 * @version 1.0.0
 * @since 2026-06-23
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class FeedbackController {

    /**
     * 作者标识（用于页面底部展示）
     */
    private static final String STUDENT_ID = "liem";

    /**
     * 学生姓名（用于页面底部展示）
     */
    private static final String STUDENT_NAME = "liem";

    /**
     * 意见反馈业务服务（通过构造器注入）
     */
    private final FeedbackService feedbackService;

    /**
     * 根路径重定向到提交页面
     *
     * @return 重定向 URL
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/submit";
    }

    /**
     * 显示意见提交页面
     *
     * @param model Spring MVC Model
     * @return 视图名称 submit
     */
    @GetMapping("/submit")
    public String submitPage(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("studentId", STUDENT_ID);
        model.addAttribute("studentName", STUDENT_NAME);
        return "submit";
    }

    /**
     * 处理意见提交表单
     *
     * <p>使用 @ModelAttribute 绑定表单数据到 Feedback 对象。</p>
     * <p>提交成功后重定向到 list.html，避免重复提交。</p>
     *
     * @param feedback 表单绑定的 Feedback 对象
     * @return 重定向到列表页
     */
    @PostMapping("/submit")
    public String submitFeedback(@ModelAttribute Feedback feedback) {
        log.info("liem(liem) 收到表单提交: username={}, content={}",
                feedback.getUsername(), feedback.getContent());
        feedbackService.submitFeedback(feedback);
        return "redirect:/list";
    }

    /**
     * 显示意见列表页面
     *
     * <p>调用 FeedbackService.listAll() 获取全部意见（带 Redis 缓存）。</p>
     *
     * @param model Spring MVC Model
     * @return 视图名称 list
     */
    @GetMapping("/list")
    public String listPage(Model model) {
        List<Feedback> feedbackList = feedbackService.listAll();
        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("studentId", STUDENT_ID);
        model.addAttribute("studentName", STUDENT_NAME);
        return "list";
    }
}
