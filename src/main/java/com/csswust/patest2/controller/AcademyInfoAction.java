package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.AcademyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/13.
 */
@RestController
@RequestMapping("/academyInfo")
public class AcademyInfoAction extends BaseAction {
    @Autowired
    private AcademyInfoDao academyInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            AcademyInfo academyInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (academyInfo == null) return null;
        Map<String, Object> res = new HashMap<>();
        List<AcademyInfo> academyInfoList = academyInfoDao.selectByCondition(academyInfo,
                new BaseQuery(page, rows));
        Integer total = academyInfoDao.selectByConditionGetCount(academyInfo, new BaseQuery());
        res.put("total", total);
        res.put("list", academyInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(AcademyInfo academyInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = academyInfoDao.insertSelective(academyInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(AcademyInfo academyInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = academyInfoDao.updateByPrimaryKeySelective(academyInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = academyInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
