package com.csswust.patest2.service.judge;

import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 杨顺丰
 */
public class JudgeTask {
    private Integer submId;
    private Integer limitMemory;
    private Integer limitTime;
    private Integer pid;
    private Integer testdataNum;
    private Integer language;
    private String source;
    private Integer judgeMode;

    public JudgeTask() {
        // TODO Auto-generated constructor stub
    }

    public JudgeTask(Integer submId, Integer limitMemory, Integer limitTime, Integer pid,
                     Integer testdataNum, Integer language, String source, Integer judgeMode) {
        super();
        this.submId = submId;
        this.limitMemory = limitMemory;
        this.limitTime = limitTime;
        this.pid = pid;
        this.testdataNum = testdataNum;
        this.language = language;
        this.source = source;
        this.judgeMode = judgeMode;
    }

    public Integer getSubmId() {
        return submId;
    }

    public void setSubmId(Integer submId) {
        this.submId = submId;
    }

    public Integer getLimitMemory() {
        return limitMemory;
    }

    public void setLimitMemory(Integer limitMemory) {
        this.limitMemory = limitMemory;
    }

    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Integer limitTime) {
        this.limitTime = limitTime;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getTestdataNum() {
        return testdataNum;
    }

    public void setTestdataNum(Integer testdataNum) {
        this.testdataNum = testdataNum;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getJudgeMode() {
        return judgeMode;
    }

    public void setJudgeMode(Integer judgeMode) {
        this.judgeMode = judgeMode;
    }

    @Override
    public String toString() {
        return "JudgeTask [submId=" + submId + ", limitMemory=" + limitMemory + ", limitTime="
                + limitTime + ", pid=" + pid + ", testdataNum=" + testdataNum + ", language="
                + language + ", source=" + source + ", judgeMode=" + judgeMode + "]";
    }

    public String verification() {
        if (submId == null || submId < 0) return "submId不能为空";
        if (pid == null || pid < 0) return "pid不能为空";
        if (StringUtils.isBlank(source)) return "source不能为空";
        Integer maxLimitMemory = Config.getToInt(SiteKey.JUDGE_MAX_LIMIT_MEMORY);
        if (limitMemory < 0) return "limitMemory不能小于0";
        if (limitMemory > maxLimitMemory) return "limitMemory不能大于maxLimitMemory";
        Integer maxLimitTime = Config.getToInt(SiteKey.JUDGE_MAX_LIMIT_TIME);
        if (limitTime < 0) return "limitTime不能小于0";
        if (limitTime > maxLimitTime) return "limitTime不能大于maxLimitTime";
        Integer maxTestNum = Config.getToInt(SiteKey.JUDGE_MAX_TEST_NUM);
        if (testdataNum < 0) return "testdataNum不能小于0";
        if (testdataNum > maxTestNum) return "testdataNum不能大于maxTestNum";
        String allowLanguage = Config.get(SiteKey.JUDGE_ALLOW_LANGUAGE);
        if (language == null) return "language不能为空";
        if (!allowLanguage.contains(String.valueOf(language)))
            return "language不在allowLanguage允许范围内";
        return null;
    }
}
