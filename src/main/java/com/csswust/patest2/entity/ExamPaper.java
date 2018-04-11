package com.csswust.patest2.entity;

import java.util.Date;

public class ExamPaper {
    private Integer exaPapId;

    private Integer examId;

    private Integer userId;

    private String classroom;

    private Double score;

    private Integer acedCount;

    private Integer usedTime;

    private Integer isMarked;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    public Integer getExaPapId() {
        return exaPapId;
    }

    public void setExaPapId(Integer exaPapId) {
        this.exaPapId = exaPapId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom == null ? null : classroom.trim();
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getAcedCount() {
        return acedCount;
    }

    public void setAcedCount(Integer acedCount) {
        this.acedCount = acedCount;
    }

    public Integer getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public Integer getIsMarked() {
        return isMarked;
    }

    public void setIsMarked(Integer isMarked) {
        this.isMarked = isMarked;
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