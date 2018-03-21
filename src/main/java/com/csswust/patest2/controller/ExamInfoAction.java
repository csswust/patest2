package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.ExamPaperDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
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
@RequestMapping("/examInfo")
public class ExamInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamInfoAction.class);

    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private ExamPaperDao examPaperDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectTempProblem(
            ExamInfo examInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        BaseQuery baseQuery = new BaseQuery();
        if (examInfo.getStartTime() != null) {
            baseQuery.setCustom("startTime", examInfo.getStartTime());
            examInfo.setStartTime(null);
        }
        if (examInfo.getEndTime() != null) {
            baseQuery.setCustom("endTime", examInfo.getEndTime());
            examInfo.setEndTime(null);
        }
        Integer total = examInfoDao.selectByConditionGetCount(examInfo, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<ExamInfo> examInfoList = examInfoDao.selectByCondition(examInfo, baseQuery);
        List<Integer> peopleTotal = new ArrayList<>();
        ExamPaper examPaper = new ExamPaper();
        for (int i = 0; i < examInfoList.size(); i++) {
            examPaper.setExamId(examInfoList.get(i).getExamId());
            int temp = examPaperDao.selectByConditionGetCount(examPaper, new BaseQuery());
            peopleTotal.add(temp);
        }
        res.put("total", total);
        res.put("examInfoList", examInfoList);
        res.put("peopleTotal", peopleTotal);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(ExamInfo examInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = examInfoDao.insertSelective(examInfo);
        res.put("status", result);
        res.put("examId", examInfo.getExamId());
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(ExamInfo examInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = examInfoDao.updateByPrimaryKeySelective(examInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = examInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
