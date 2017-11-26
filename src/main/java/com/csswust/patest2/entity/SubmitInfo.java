package com.csswust.patest2.entity;

import java.util.Date;

public class SubmitInfo {
    private Integer submId;

    private Integer userId;

    private Integer examId;

    private Integer examPaperId;

    private Integer examParamId;

    private Integer paperProblemId;

    private Integer problemId;

    private Integer judgerId;

    private String ip;

    private Integer isTeacherTest;

    private Integer status;

    private Integer usedTime;

    private Integer usedMemory;

    private Date createTime;

    private Date modifyTime;

    private String source;

    private String errMsg;

    public Integer getSubmId() {
        return submId;
    }

    public void setSubmId(Integer submId) {
        this.submId = submId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Integer examPaperId) {
        this.examPaperId = examPaperId;
    }

    public Integer getExamParamId() {
        return examParamId;
    }

    public void setExamParamId(Integer examParamId) {
        this.examParamId = examParamId;
    }

    public Integer getPaperProblemId() {
        return paperProblemId;
    }

    public void setPaperProblemId(Integer paperProblemId) {
        this.paperProblemId = paperProblemId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getJudgerId() {
        return judgerId;
    }

    public void setJudgerId(Integer judgerId) {
        this.judgerId = judgerId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getIsTeacherTest() {
        return isTeacherTest;
    }

    public void setIsTeacherTest(Integer isTeacherTest) {
        this.isTeacherTest = isTeacherTest;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public Integer getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Integer usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg == null ? null : errMsg.trim();
    }
}