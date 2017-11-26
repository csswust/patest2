package com.csswust.patest2.entity;

import java.util.Date;

public class ProblemKnowledge {
    private Integer proKnoId;

    private Integer probblemId;

    private Integer knowledgeId;

    private Date createTime;

    private Date modifyTime;

    public Integer getProKnoId() {
        return proKnoId;
    }

    public void setProKnoId(Integer proKnoId) {
        this.proKnoId = proKnoId;
    }

    public Integer getProbblemId() {
        return probblemId;
    }

    public void setProbblemId(Integer probblemId) {
        this.probblemId = probblemId;
    }

    public Integer getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Integer knowledgeId) {
        this.knowledgeId = knowledgeId;
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