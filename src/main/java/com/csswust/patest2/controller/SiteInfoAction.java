package com.csswust.patest2.controller;

import com.csswust.patest2.dao.SiteInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.SiteInfo;
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
@RequestMapping("/siteInfo")
public class SiteInfoAction {
    @Autowired
    private SiteInfoDao siteInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            SiteInfo siteInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        List<SiteInfo> siteInfoList = siteInfoDao.selectByCondition(siteInfo,
                new BaseQuery(page, rows));
        Integer total = siteInfoDao.selectByConditionGetCount(siteInfo, new BaseQuery());
        res.put("total", total);
        res.put("data", siteInfoList);
        res.put("status", 1);
        return res;
    }

    @RequestMapping(value = "/updateAll", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateAll(Integer[] siteIds, String[] values) {
        Map<String, Object> res = new HashMap<>();
        int result = 0;
        for (int i = 0; i < siteIds.length; i++) {
            SiteInfo siteInfo = new SiteInfo();
            siteInfo.setSiteId(siteIds[i]);
            siteInfo.setValue(values[i]);
            result += siteInfoDao.updateByPrimaryKeySelective(siteInfo);
        }
        res.put("status", result);
        return res;
    }
}
