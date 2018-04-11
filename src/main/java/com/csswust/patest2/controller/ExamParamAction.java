package com.csswust.patest2.controller;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.CourseInfoDao;
import com.csswust.patest2.dao.ExamParamDao;
import com.csswust.patest2.dao.KnowledgeInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.CourseInfo;
import com.csswust.patest2.entity.ExamParam;
import com.csswust.patest2.entity.KnowledgeInfo;
import com.csswust.patest2.service.ExamParamService;
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
@RequestMapping("/examParam")
public class ExamParamAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamParamAction.class);

    @Autowired
    private ExamParamDao examParamDao;
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private ExamParamService examParamService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            ExamParam examParam,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (examParam == null) return null;
        Map<String, Object> res = new HashMap<>();
        Integer total = examParamDao.selectByConditionGetCount(examParam, new BaseQuery());
        List<ExamParam> examParamList = examParamDao.selectByCondition(examParam, new BaseQuery(page, rows));
        List<KnowledgeInfo> knowledgeInfoList = selectRecordByIds(
                getFieldByList(examParamList, "knowId", ExamParam.class),
                "knowId", (BaseDao) knowledgeInfoDao, KnowledgeInfo.class);
        List<CourseInfo> courseInfoList = selectRecordByIds(
                getFieldByList(knowledgeInfoList, "courseId", KnowledgeInfo.class),
                "couId", (BaseDao) courseInfoDao, CourseInfo.class);
        List<Integer> problemSumList = new ArrayList<>();
        for (int i = 0; i < examParamList.size(); i++) {
            ExamParam temp = new ExamParam();
            temp.setExamId(examParamList.get(i).getExamId());
            temp.setKnowId(examParamList.get(i).getKnowId());
            temp.setLevelId(examParamList.get(i).getLevelId());
            int count = examParamDao.getProblemNum(temp, new BaseQuery());
            problemSumList.add(count);
        }
        res.put("total", total);
        res.put("examParamList", examParamList);
        res.put("problemSumList", problemSumList);
        res.put("knowledgeInfoList", knowledgeInfoList);
        res.put("courseInfoList", courseInfoList);
        return res;
    }

    @RequestMapping(value = "/selectProblemTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectProblemTotal(
            @RequestParam(required = false) Integer knowId,
            @RequestParam(required = false) Integer levelId,
            @RequestParam Integer examId) {
        if (examId == null) return null;
        Map<String, Object> res = new HashMap<>();
        ExamParam temp = new ExamParam();
        temp.setExamId(examId);
        temp.setKnowId(knowId);
        temp.setLevelId(levelId);
        int count = examParamDao.getProblemNum(temp, new BaseQuery());
        res.put("total", count);
        return res;
    }

    @RequestMapping(value = "/insertByArray", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertByArray(
            @RequestParam Integer examId,
            @RequestParam Integer[] knowIds,
            @RequestParam Integer[] levels,
            @RequestParam Integer[] scores) {
        if (examId == null) return null;
        Map<String, Object> res = new HashMap<>();
        APIResult result = examParamService.insertByArray(examId, knowIds, levels, scores);
        res.put("APIResult", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = examParamDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}