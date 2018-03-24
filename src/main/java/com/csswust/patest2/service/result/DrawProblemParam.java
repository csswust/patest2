package com.csswust.patest2.service.result;

import com.csswust.patest2.entity.ExamParam;
import com.csswust.patest2.entity.ExamProblem;
import com.csswust.patest2.entity.ProblemInfo;

import java.util.List;

/**
 * Created by 972536780 on 2018/3/21.
 */
public class DrawProblemParam {
    private ExamParam param;// 题目模板
    private List<ExamProblem> examProblemList;// 满足条件的题目
    private List<ProblemInfo> problemInfoList;

    public ExamParam getParam() {
        return param;
    }

    public void setParam(ExamParam param) {
        this.param = param;
    }

    public List<ExamProblem> getExamProblemList() {
        return examProblemList;
    }

    public void setExamProblemList(List<ExamProblem> examProblemList) {
        this.examProblemList = examProblemList;
    }

    public List<ProblemInfo> getProblemInfoList() {
        return problemInfoList;
    }

    public void setProblemInfoList(List<ProblemInfo> problemInfoList) {
        this.problemInfoList = problemInfoList;
    }
}
