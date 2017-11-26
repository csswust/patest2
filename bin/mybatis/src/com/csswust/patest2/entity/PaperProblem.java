package com.csswust.patest2.entity;

import java.util.Date;

public class PaperProblem {
    private Integer papProId;

    private Integer examId;

    private Integer examPaperId;

    private Integer examParamId;

    private Integer problemId;

    private Integer order;

    private Date lastSubmitTime;

    private Integer isAced;

    private Integer usedTime;

    private Integer submitCount;

    private Integer score;

    private Date createTime;

    private Date modifyTime;

    public Integer getPapProId() {
        return papProId;
    }

    public void setPapProId(Integer papProId) {
        this.papProId = papProId;
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

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getLastSubmitTime() {
        return lastSubmitTime;
    }

    public void setLastSubmitTime(Date lastSubmitTime) {
        this.lastSubmitTime = lastSubmitTime;
    }

    public Integer getIsAced() {
        return isAced;
    }

    public void setIsAced(Integer isAced) {
        this.isAced = isAced;
    }

    public Integer getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public Integer getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(Integer submitCount) {
        this.submitCount = submitCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
}