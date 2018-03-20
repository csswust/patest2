package com.csswust.patest2.listener;

import com.csswust.patest2.service.JudgeService;
import com.csswust.patest2.service.judge.JudgeThread;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 项目启动监听器
 *
 * @author 杨顺丰
 */
@WebListener
public class ApplicationStartListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent servletContext) {

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContext) {
        ApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(servletContext.getServletContext());
        JudgeService judgeService = context.getBean(JudgeService.class);
        JudgeThread judgeThread = JudgeThread.getInstance();
        judgeThread.setJudgeService(judgeService);
        new Thread(judgeThread).start();
    }
}
