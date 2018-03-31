package com.csswust.patest2.entity;

import java.util.Date;

public class ProblemSubmit {
    private Integer id;

    private Integer userId;

    private Integer problemId;

    private Date submitTime;

    private Short status;

    private Integer timeUsed;

    private Integer memoryUsed;

    private Integer judgerId;

    private Integer examId;

    private Integer paperId;

    private Integer paperProblemId;

    private String ip;

    private Integer printRequest;

    private Integer newSubmId;

    private Integer newStatus;

    private String source;

    private String extra;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(Integer timeUsed) {
        this.timeUsed = timeUsed;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public Integer getJudgerId() {
        return judgerId;
    }

    public void setJudgerId(Integer judgerId) {
        this.judgerId = judgerId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getPaperProblemId() {
        return paperProblemId;
    }

    public void setPaperProblemId(Integer paperProblemId) {
        this.paperProblemId = paperProblemId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getPrintRequest() {
        return printRequest;
    }

    public void setPrintRequest(Integer printRequest) {
        this.printRequest = printRequest;
    }

    public Integer getNewSubmId() {
        return newSubmId;
    }

    public void setNewSubmId(Integer newSubmId) {
        this.newSubmId = newSubmId;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra == null ? null : extra.trim();
    }
}