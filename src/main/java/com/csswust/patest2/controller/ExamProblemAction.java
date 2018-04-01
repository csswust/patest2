package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.CourseInfoDao;
import com.csswust.patest2.dao.ExamProblemDao;
import com.csswust.patest2.dao.KnowledgeInfoDao;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.CourseInfo;
import com.csswust.patest2.entity.ExamProblem;
import com.csswust.patest2.entity.KnowledgeInfo;
import com.csswust.patest2.entity.ProblemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/19.
 */
@RestController
@RequestMapping("/examProblem")
public class ExamProblemAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamProblemAction.class);

    @Autowired
    private ExamProblemDao examProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            @RequestParam(required = true) Integer examId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
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
        res.put("total", total);
        res.put("examProblemList", examProblemList);
        res.put("problemInfoList", problemInfoList);
        res.put("knowledgeInfoList", knowledgeInfoList);
        res.put("courseInfoList", courseInfoList);
        return res;
    }

    @RequestMapping(value = "/insertByArray", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertByArray(
            @RequestParam(required = true) Integer examId,
            @RequestParam(required = true) Integer[] probIdList) {
        Map<String, Object> res = new HashMap<>();
        if (examId == null) return null;
        if (probIdList == null || probIdList.length == 0) {
            res.put("desc", "题目不能为空");
            return res;
        }
        List<ExamProblem> examProblemList = new ArrayList<>();
        for (int i = 0; i < probIdList.length; i++) {
            ExamProblem examProblem = new ExamProblem();
            examProblem.setExamId(examId);
            examProblem.setProblemId(probIdList[i]);
            examProblemList.add(examProblem);
        }
        int count = examProblemDao.insertBatch(examProblemList);
        if (count != probIdList.length) res.put("status", 0);
        else res.put("status", count);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = examProblemDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
