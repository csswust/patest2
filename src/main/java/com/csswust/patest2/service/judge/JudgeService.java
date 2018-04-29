package com.csswust.patest2.service.judge;


import java.util.Map;

/**
 * @author 杨顺丰
 */
public interface JudgeService {
    JudgeTask getTaskBySubmId(Integer submId);

    Map<String, Object> refresh(JudgeTask judgeTask, JudgeResult judgeResult);

    void refreshPaperById(Integer papProId);

    JudgeResult judge(JudgeTask judgeTask);
}
