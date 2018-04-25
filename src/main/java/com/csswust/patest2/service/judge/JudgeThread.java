package com.csswust.patest2.service.judge;

import com.csswust.patest2.common.MonitorKey;
import com.csswust.patest2.service.monitor.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.csswust.patest2.listener.ApplicationStartListener.queue;
import static com.csswust.patest2.listener.ApplicationStartListener.refreshExecutor;

/**
 * @author 杨顺丰
 */
public class JudgeThread implements Runnable {
    private static Logger log = LoggerFactory.getLogger(JudgeThread.class);

    private volatile Integer currSubmId;
    private volatile Date startDate;

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                log.info("判题线程正常结束：{}", Thread.currentThread().hashCode());
                break;
            }
            try {
                // 阻塞方法，如果队列为空则阻塞
                Integer submId = queue.take();
                long startTime = System.currentTimeMillis();
                setCurrSubmId(submId);
                setStartDate(new Date());
                // 根据id生成任务
                JudgeTask judgeTask = judgeService.getTaskBySubmId(submId);
                // 提交任务
                JudgeResult judgeResult = judgeService.judge(judgeTask);
                // 刷新试卷
                refreshExecutor.execute(() -> {
                    judgeService.refresh(judgeTask, judgeResult);
                });
                long endTime = System.currentTimeMillis();
                log.info("submId: {}, time: {}ms", submId, endTime - startTime);
                monitor.addSize(MonitorKey.JUDGE_RESPONSE_TIME.getKey(), (int) (endTime - startTime));
                setCurrSubmId(null);
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                } else {
                    log.error("JudgeThread 判题任务队列异常：{}", e);
                }
            }
        }
    }

    private JudgeService judgeService;
    private Monitor monitor;

    public JudgeThread(JudgeService judgeService) {
        super();
        this.judgeService = judgeService;
    }

    public JudgeService getJudgeService() {
        return judgeService;
    }

    public void setJudgeService(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public Integer getCurrSubmId() {
        return currSubmId;
    }

    public void setCurrSubmId(Integer currSubmId) {
        this.currSubmId = currSubmId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
