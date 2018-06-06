package com.csswust.patest2.entity;

import java.util.Date;

public class ExamInfo {
    private Integer examId;

    private String title;

    private Date startTime;

    private Date endTime;

    private String allowIp;

    private Integer isDrawProblem;

    private Integer isSimTest;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    private String description;

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAllowIp() {
        return allowIp;
    }

    public void setAllowIp(String allowIp) {
        this.allowIp = allowIp == null ? null : allowIp.trim();
    }

    public Integer getIsDrawProblem() {
        return isDrawProblem;
    }

    public void setIsDrawProblem(Integer isDrawProblem) {
        this.isDrawProblem = isDrawProblem;
    }

    public Integer getIsSimTest() {
        return isSimTest;
    }

    public void setIsSimTest(Integer isSimTest) {
        this.isSimTest = isSimTest;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}