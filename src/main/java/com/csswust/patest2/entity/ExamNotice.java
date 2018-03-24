package com.csswust.patest2.entity;

import java.util.Date;

public class ExamNotice {
    private Integer exaNotId;

    private Integer examId;

    private String title;

    private Date createTime;

    private Date modifyTime;

    private String content;

    public Integer getExaNotId() {
        return exaNotId;
    }

    public void setExaNotId(Integer exaNotId) {
        this.exaNotId = exaNotId;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}