package com.csswust.patest2.service.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2018/4/28.
 */
public class OperateLogInsert {
    private Integer userId;

    private String ip;

    private String apiUrl;

    private String apiDesc;

    private Map<String, Object> argc = new HashMap<>();

    private Integer examId;

    private Integer problemId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public Map<String, Object> getArgc() {
        return argc;
    }

    public void setArgc(Map<String, Object> argc) {
        this.argc = argc;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public void setArgcData(String key, Object value) {
        argc.put(key, value);
    }
}
