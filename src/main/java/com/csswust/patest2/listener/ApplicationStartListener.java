package com.csswust.patest2.listener;

import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.service.JudgeService;
import com.csswust.patest2.service.judge.JudgeThread;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目启动监听器
 *
 * @author 杨顺丰
 */
@WebListener
public class ApplicationStartListener implements ServletContextListener {
    // 判题线程池
    public static ExecutorService judgeExecutor = Executors.newFixedThreadPool(
            Config.getToInt(SiteKey.JUDGE_THREAD_POOL_NUM));
    // 更新试卷线程池
    public static ExecutorService refreshExecutor = Executors.newFixedThreadPool(
            Config.getToInt(SiteKey.JUDGE_THREAD_POOL_NUM));
    // 任务队列
    public static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(
            Config.getToInt(SiteKey.JUDGE_TASK_QUEUE_TOTAL), true);

    public static List<JudgeThread> judgeThreadList = new ArrayList<>();

    @Override
    public void contextDestroyed(ServletContextEvent servletContext) {

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContext) {
        ApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(servletContext.getServletContext());
        JudgeService judgeService = context.getBean(JudgeService.class);
        int num = Config.getToInt(SiteKey.JUDGE_THREAD_POOL_NUM);
        // 启动判题线程
        for (int i = 0; i < num; i++) {
            JudgeThread judgeThread = new JudgeThread(judgeService);
            judgeThreadList.add(judgeThread);
            judgeExecutor.execute(judgeThread);
        }
    }
}
