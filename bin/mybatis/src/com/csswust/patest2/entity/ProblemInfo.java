package com.csswust.patest2.entity;

import java.util.Date;

public class ProblemInfo {
    private Integer probId;

    private String title;

    private Integer levelId;

    private Integer memoryLimit;

    private Integer timeLimit;

    private Integer codeLimit;

    private Integer testdataNum;

    private String scoreRatio;

    private Integer judgeModel;

    private String author;

    private Integer submitNum;

    private Integer acceptedNum;

    private Date createTime;

    private Date modifyTime;

    private String description;

    private String inputTip;

    private String outputTip;

    private String inputSample;

    private String outputSample;

    private String standardSource;

    public Integer getProbId() {
        return probId;
    }

    public void setProbId(Integer probId) {
        this.probId = probId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getCodeLimit() {
        return codeLimit;
    }

    public void setCodeLimit(Integer codeLimit) {
        this.codeLimit = codeLimit;
    }

    public Integer getTestdataNum() {
        return testdataNum;
    }

    public void setTestdataNum(Integer testdataNum) {
        this.testdataNum = testdataNum;
    }

    public String getScoreRatio() {
        return scoreRatio;
    }

    public void setScoreRatio(String scoreRatio) {
        this.scoreRatio = scoreRatio == null ? null : scoreRatio.trim();
    }

    public Integer getJudgeModel() {
        return judgeModel;
    }

    public void setJudgeModel(Integer judgeModel) {
        this.judgeModel = judgeModel;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Integer getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(Integer submitNum) {
        this.submitNum = submitNum;
    }

    public Integer getAcceptedNum() {
        return acceptedNum;
    }

    public void setAcceptedNum(Integer acceptedNum) {
        this.acceptedNum = acceptedNum;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getInputTip() {
        return inputTip;
    }

    public void setInputTip(String inputTip) {
        this.inputTip = inputTip == null ? null : inputTip.trim();
    }

    public String getOutputTip() {
        return outputTip;
    }

    public void setOutputTip(String outputTip) {
        this.outputTip = outputTip == null ? null : outputTip.trim();
    }

    public String getInputSample() {
        return inputSample;
    }

    public void setInputSample(String inputSample) {
        this.inputSample = inputSample == null ? null : inputSample.trim();
    }

    public String getOutputSample() {
        return outputSample;
    }

    public void setOutputSample(String outputSample) {
        this.outputSample = outputSample == null ? null : outputSample.trim();
    }

    public String getStandardSource() {
        return standardSource;
    }

    public void setStandardSource(String standardSource) {
        this.standardSource = standardSource == null ? null : standardSource.trim();
    }
}