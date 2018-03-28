package com.csswust.patest2.service.judge;

import com.csswust.patest2.service.JudgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 判题主线程，特别警告：此线程只能开启一个
 *
 * @author 杨顺丰
 */
public class JudgeThread implements Runnable {
    private static Logger log = LoggerFactory.getLogger(JudgeThread.class);

    // 任务队列
    public static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10000, true);
    // 更新试卷线程池
    public static ExecutorService executor = Executors.newFixedThreadPool(5);

    @Override
    public void run() {
        while (true) {
            try {
                // 阻塞方法，如果队列为空则阻塞
                Integer submId = queue.take();
                // 根据id生成任务
                JudgeTask judgeTask = judgeService.getTaskBySubmId(submId);
                // 提交任务
                JudgeResult judgeResult = judgeService.judge(judgeTask);
                // 刷新试卷
                executor.execute(() -> {
                    judgeService.refresh(judgeTask, judgeResult);
                });
            } catch (Exception e) {
                log.error("JudgeThread 判题任务队列异常：{}", e);
            }
        }
    }

    private JudgeService judgeService;

    private JudgeThread() {
        super();
    }

    private static class Single {
        private final static JudgeThread INSTANCE = new JudgeThread();
    }

    public static JudgeThread getInstance() {
        return Single.INSTANCE;
    }

    public JudgeService getJudgeService() {
        return judgeService;
    }

    public void setJudgeService(JudgeService judgeService) {
        this.judgeService = judgeService;
    }
}
