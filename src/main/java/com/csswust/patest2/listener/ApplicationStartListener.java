package com.csswust.patest2.listener;

import com.csswust.patest2.common.cache.SiteCache;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.SiteInfoDao;
import com.csswust.patest2.service.judge.JudgeService;
import com.csswust.patest2.service.judge.JudgeThread;
import com.csswust.patest2.service.monitor.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.TimeUnit;

/**
 * 项目启动监听器
 *
 * @author 杨顺丰
 */
@WebListener
public class ApplicationStartListener implements ServletContextListener {
    private static Logger log = LoggerFactory.getLogger(ApplicationStartListener.class);

    // 判题线程池
    public static ExecutorService judgeExecutor;
    // 更新试卷线程池
    public static ExecutorService refreshExecutor;
    // 任务队列
    public static ArrayBlockingQueue<Integer> queue;

    public static List<JudgeThread> judgeThreadList = new ArrayList<>();

    @Override
    public void contextDestroyed(ServletContextEvent servletContext) {
        // 中断当前的，发送interrupt标记
        judgeExecutor.shutdownNow();
        // 移除队列中的线程，中断当前的（不一定能中断）
        refreshExecutor.shutdownNow();
        boolean judge = false, refresh = false;
        try {
            judge = judgeExecutor.awaitTermination(100, TimeUnit.SECONDS);
            refresh = refreshExecutor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("awaitTermination error: {}", e);
        }
        log.info("contextDestroyed info judgeExecutor:{} refreshExecutor: {}",
                judge, refresh);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContext) {
        ApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(servletContext.getServletContext());
        // 获取配置文件和siteInfo
        Config.refreshConfig();
        SiteInfoDao siteInfoDao = context.getBean(SiteInfoDao.class);
        Config.refreshSiteInfo(siteInfoDao, null);

        // 初始化判题线程池和更新试卷线程池
        int num = Config.getToInt(SiteKey.JUDGE_THREAD_POOL_NUM, SiteKey.JUDGE_THREAD_POOL_NUM_DE);
        judgeExecutor = Executors.newFixedThreadPool(num);
        refreshExecutor = Executors.newFixedThreadPool(num);
        JudgeService judgeService = context.getBean(JudgeService.class);
        Monitor monitor = context.getBean(Monitor.class);
        queue = new ArrayBlockingQueue<>(Config.getToInt(SiteKey.JUDGE_TASK_QUEUE_TOTAL, SiteKey.JUDGE_TASK_QUEUE_TOTAL_DE), true);
        // 启动判题线程
        for (int i = 0; i < num; i++) {
            JudgeThread judgeThread = new JudgeThread(judgeService);
            judgeThread.setMonitor(monitor);
            judgeThreadList.add(judgeThread);
            judgeExecutor.execute(judgeThread);
        }

        // 对result和judger进行缓存
        SiteCache siteCache = context.getBean(SiteCache.class);
        siteCache.refresh();
    }
}
