package com.csswust.patest2.service.judge;

/**
 * @author 杨顺丰
 */
public class JudgeTask {
	private Integer submId;
	private Integer limitMemory;
	private Integer limitTime;
	private Integer pid;
	private Integer testdataNum;
	private Integer language;
	private String source;
	private Integer judgeMode;
	
	public JudgeTask() {
		// TODO Auto-generated constructor stub
	}
	
	public JudgeTask(Integer submId, Integer limitMemory, Integer limitTime, Integer pid,
			Integer testdataNum, Integer language, String source, Integer judgeMode) {
		super();
		this.submId = submId;
		this.limitMemory = limitMemory;
		this.limitTime = limitTime;
		this.pid = pid;
		this.testdataNum = testdataNum;
		this.language = language;
		this.source = source;
		this.judgeMode = judgeMode;
	}

	public Integer getSubmId() {
		return submId;
	}

	public void setSubmId(Integer submId) {
		this.submId = submId;
	}

	public Integer getLimitMemory() {
		return limitMemory;
	}

	public void setLimitMemory(Integer limitMemory) {
		this.limitMemory = limitMemory;
	}

	public Integer getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(Integer limitTime) {
		this.limitTime = limitTime;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getTestdataNum() {
		return testdataNum;
	}

	public void setTestdataNum(Integer testdataNum) {
		this.testdataNum = testdataNum;
	}

	public Integer getLanguage() {
		return language;
	}

	public void setLanguage(Integer language) {
		this.language = language;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getJudgeMode() {
		return judgeMode;
	}

	public void setJudgeMode(Integer judgeMode) {
		this.judgeMode = judgeMode;
	}
	
	@Override
	public String toString() {
		return "JudgeTask [submId=" + submId + ", limitMemory=" + limitMemory + ", limitTime="
				+ limitTime + ", pid=" + pid + ", testdataNum=" + testdataNum + ", language="
				+ language + ", source=" + source + ", judgeMode=" + judgeMode + "]";
	}
}
