package com.csswust.patest2.controller;

import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserAction extends BaseAction {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/user/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insert(UserInfo userInfo) {
        Map<String, Object> res = new HashMap<String, Object>();
        int result = userInfoService.insert(userInfo);
        res.put("status", result);
        return res;
    }
}
