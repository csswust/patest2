package com.csswust.patest2.vo;

import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.entity.UserProfile;

import java.util.List;

/**
 * Create by Wzy
 * on 2018/7/4 18:30
 * 不短不长八字刚好
 */
public class PersonExamPaper {
    private ExamInfo examInfo;
    private UserProfile userProfile;//考生信息
    private ExamPaper examPaper;//试卷信息
    private List<ProblemInfo> problemInfos;//问题集合
    private List<String> status;
    private List<Integer> allScore;
    private List<Double> score;

    public PersonExamPaper(ExamInfo examInfo, UserProfile userProfile, ExamPaper examPaper, List<ProblemInfo> problemInfos, List<String> status, List<Integer> allScore, List<Double> score) {
        this.examInfo = examInfo;
        this.userProfile = userProfile;
        this.examPaper = examPaper;
        this.problemInfos = problemInfos;
        this.status = status;
        this.allScore = allScore;
        this.score = score;
    }

    public ExamInfo getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ExamInfo examInfo) {
        this.examInfo = examInfo;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public ExamPaper getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }

    public List<ProblemInfo> getProblemInfos() {
        return problemInfos;
    }

    public void setProblemInfos(List<ProblemInfo> problemInfos) {
        this.problemInfos = problemInfos;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<Integer> getAllScore() {
        return allScore;
    }

    public void setAllScore(List<Integer> allScore) {
        this.allScore = allScore;
    }

    public List<Double> getScore() {
        return score;
    }

    public void setScore(List<Double> score) {
        this.score = score;
    }
}
