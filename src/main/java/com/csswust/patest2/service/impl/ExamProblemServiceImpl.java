package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.lexam.ExamProblemAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamProblemService;
import com.csswust.patest2.service.common.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/4/18.
 */
@Service
public class ExamProblemServiceImpl extends BaseService implements ExamProblemService {
    private static Logger log = LoggerFactory.getLogger(ExamProblemServiceImpl.class);

    @Autowired
    private ExamProblemDao examProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private ExamInfoDao examInfoDao;

    @Override
    public APIResult selectByCondition(Integer examId, Integer page, Integer rows) {
        APIResult apiResult = new APIResult();
        if (examId == null) return apiResult;
        ExamProblem examProblem = new ExamProblem();
        examProblem.setExamId(examId);
        Integer total = examProblemDao.selectByConditionGetCount(examProblem, new BaseQuery());
        List<ExamProblem> examProblemList = examProblemDao.selectByCondition(examProblem, new BaseQuery(page, rows));
        List<ProblemInfo> problemInfoList = selectRecordByIds(
                getFieldByList(examProblemList, "problemId", ExamProblem.class),
                "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
        List<KnowledgeInfo> knowledgeInfoList = selectRecordByIds(
                getFieldByList(problemInfoList, "knowId", ProblemInfo.class),
                "knowId", (BaseDao) knowledgeInfoDao, KnowledgeInfo.class);
        List<CourseInfo> courseInfoList = selectRecordByIds(
                getFieldByList(knowledgeInfoList, "courseId", KnowledgeInfo.class),
                "couId", (BaseDao) courseInfoDao, CourseInfo.class);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("examProblemList", examProblemList);
        apiResult.setDataKey("problemInfoList", problemInfoList);
        apiResult.setDataKey("knowledgeInfoList", knowledgeInfoList);
        apiResult.setDataKey("courseInfoList", courseInfoList);
        return apiResult;
    }

    @Override
    public APIResult insertByArray(Integer examId, Integer[] probIdList) {
        APIResult apiResult = new APIResult();
        if (examId == null) return apiResult;
        if (probIdList == null || probIdList.length == 0) {
            apiResult.setStatusAndDesc(-1, "题目不能为空");
            return apiResult;
        }
        List<ExamProblem> examProblemList = new ArrayList<>();
        for (int i = 0; i < probIdList.length; i++) {
            ExamProblem examProblem = new ExamProblem();
            examProblem.setExamId(examId);
            examProblem.setProblemId(probIdList[i]);
            examProblemList.add(examProblem);
        }
        int count = examProblemDao.insertBatch(examProblemList);
        if (count != probIdList.length) {
            apiResult.setStatusAndDesc(-2, "插入失败");
        } else {
            apiResult.setStatusAndDesc(count, "插入成功");
            ExamInfo examInfo = new ExamInfo();
            examInfo.setExamId(examId);
            examInfoDao.updateByPrimaryKeySelective(examInfo);
        }
        return apiResult;
    }

    @Override
    public APIResult deleteByIds(String ids) {
        APIResult apiResult = new APIResult();
        int result = examProblemDao.deleteByIds(ids);
        if (result == 0) {
            apiResult.setStatusAndDesc(-1, "删除失败");
        } else {
            apiResult.setStatusAndDesc(result, "删除成功");
        }
        return apiResult;
    }
}
