package com.csswust.patest2.test.yaliTest;

import com.csswust.patest2.entity.PaperProblem;
import com.csswust.patest2.entity.ProblemInfo;

import java.util.List;

/**
 * Created by 972536780 on 2018/4/2.
 */
public class MyProblem {
    private List<ProblemInfo> problemInfoList;
    private List<PaperProblem> paperProblemList;

    public List<ProblemInfo> getProblemInfoList() {
        return problemInfoList;
    }

    public void setProblemInfoList(List<ProblemInfo> problemInfoList) {
        this.problemInfoList = problemInfoList;
    }

    public List<PaperProblem> getPaperProblemList() {
        return paperProblemList;
    }

    public void setPaperProblemList(List<PaperProblem> paperProblemList) {
        this.paperProblemList = paperProblemList;
    }
}
