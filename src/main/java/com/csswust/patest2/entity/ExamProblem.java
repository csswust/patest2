package com.csswust.patest2.entity;

import java.util.Date;

public class ExamProblem {
    private Integer exaProId;

    private Integer problemId;

    private Integer examId;

    private Date createTime;

    private Date modifyTime;

    public Integer getExaProId() {
        return exaProId;
    }

    public void setExaProId(Integer exaProId) {
        this.exaProId = exaProId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
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