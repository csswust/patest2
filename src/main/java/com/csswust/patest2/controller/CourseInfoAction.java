package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.CourseInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.CourseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/18.
 */
@RestController
@RequestMapping("/courseInfo")
public class CourseInfoAction extends BaseAction {
    @Autowired
    private CourseInfoDao courseInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            CourseInfo courseInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (courseInfo == null) return null;
        Map<String, Object> res = new HashMap<>();
        List<CourseInfo> courseInfoList = courseInfoDao.selectByCondition(courseInfo,
                new BaseQuery(page, rows));
        Integer total = courseInfoDao.selectByConditionGetCount(courseInfo, new BaseQuery());
        res.put("total", total);
        res.put("list", courseInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(CourseInfo courseInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = courseInfoDao.insertSelective(courseInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(CourseInfo courseInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = courseInfoDao.updateByPrimaryKeySelective(courseInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = courseInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
