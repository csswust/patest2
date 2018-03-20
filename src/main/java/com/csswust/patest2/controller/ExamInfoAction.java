package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.entity.ExamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/19.
 */
@RestController
@RequestMapping("/examInfo")
public class ExamInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamInfoAction.class);

    @Autowired
    private ExamInfoDao examInfoDao;

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
