package com.csswust.patest2.controller;

import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.dao.MajorInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.AcademyInfo;
import com.csswust.patest2.entity.MajorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/14.
 */
@RestController
@RequestMapping("/majorInfo")
public class MajorInfoAction {
    @Autowired
    private MajorInfoDao majorInfoDao;
    @Autowired
    private AcademyInfoDao academyInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            MajorInfo majorInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (majorInfo == null) return null;
        Map<String, Object> res = new HashMap<>();
        List<MajorInfo> majorInfoList = majorInfoDao.selectByCondition(majorInfo,
                new BaseQuery(page, rows));
        Integer total = majorInfoDao.selectByConditionGetCount(majorInfo, new BaseQuery());
        List<AcademyInfo> academyInfoList = selectRecordByIds(
                getFieldByList(majorInfoList, "academyId", MajorInfo.class),
                "acaId", (BaseDao) academyInfoDao, AcademyInfo.class);
        res.put("total", total);
        res.put("list", majorInfoList);
        res.put("academyInfoList", academyInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(MajorInfo majorInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = majorInfoDao.insertSelective(majorInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(MajorInfo majorInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = majorInfoDao.updateByPrimaryKeySelective(majorInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = majorInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
