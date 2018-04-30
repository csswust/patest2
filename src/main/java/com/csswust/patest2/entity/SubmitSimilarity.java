package com.csswust.patest2.entity;

import java.util.Date;

public class SubmitSimilarity {
    private Integer subSimId;

    private Integer examId;

    private Integer problemId;

    private Integer submitId1;

    private Integer submitId2;

    private Double similarity;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    public Integer getSubSimId() {
        return subSimId;
    }

    public void setSubSimId(Integer subSimId) {
        this.subSimId = subSimId;
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

    public Integer getSubmitId1() {
        return submitId1;
    }

    public void setSubmitId1(Integer submitId1) {
        this.submitId1 = submitId1;
    }

    public Integer getSubmitId2() {
        return submitId2;
    }

    public void setSubmitId2(Integer submitId2) {
        this.submitId2 = submitId2;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
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

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
}