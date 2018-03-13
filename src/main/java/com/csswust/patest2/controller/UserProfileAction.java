package com.csswust.patest2.controller;

import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/12.
 */
@RestController
@RequestMapping("/userProfile")
public class UserProfileAction extends BaseAction {
    @Autowired
    private UserProfileDao userProfileDao;

    @RequestMapping(value = "/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertuserProfile(UserProfile userProfile) {
        Map<String, Object> res = new HashMap<String, Object>();
        int result = userProfileDao.insertSelective(userProfile);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(UserProfile userProfile) {
        Map<String, Object> res = new HashMap<String, Object>();
        int result = userProfileDao.updateByPrimaryKeySelective(userProfile);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(
            @RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<String, Object>();
        int result = userProfileDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
