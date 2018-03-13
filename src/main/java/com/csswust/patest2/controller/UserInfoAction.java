package com.csswust.patest2.controller;

import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.UserInfoService;
import com.csswust.patest2.service.result.LoginRe;
import com.csswust.patest2.service.result.UserInfoInsertRe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserInfoAction extends BaseAction {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> login(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        LoginRe result = userInfoService.login(username, password, getIp(request));
        // 进行实际登录
        Map<String, Object> sessionMap = new HashMap<>();
        if (result.getCurrUser() != null) {
            UserInfo userInfo = result.getCurrUser();
            sessionMap.put("isTeacher", userInfo.getIsTeacher());
            sessionMap.put("isAdmin", userInfo.getIsAdmin());
            sessionMap.put("userId", userInfo.getUserId());
            sessionMap.put("username", userInfo.getUsername());
        }
        if (result.getUserProfile() != null) {
            UserProfile userProfile = result.getUserProfile();
            sessionMap.put("realName", userProfile.getRealName());
            sessionMap.put("studentNumber", userProfile.getStudentNumber());
        }
        if (result.getExamInfo() != null) {
            ExamInfo examInfo = result.getExamInfo();
            sessionMap.put("title", examInfo.getTitle());
            sessionMap.put("examId", examInfo.getExamId());
            sessionMap.put("startTime", examInfo.getStartTime());
            sessionMap.put("endTime", examInfo.getEndTime());
        }
        if (result.getStatus() != 1 || result.getCurrUser() == null) {
            sessionMap.put("userPermisson", "not_login");
        } else if (result.getCurrUser().getIsAdmin() == 1) {
            sessionMap.put("userPermisson", "admin");
        } else if (result.getCurrUser().getIsTeacher() == 1) {
            sessionMap.put("userPermisson", "teacher");
        } else {
            sessionMap.put("userPermisson", "student");
        }
        if (result.getStatus() == 1) {
            this.clearSession(request);
            this.saveSession(request, sessionMap);
        }
        res.put("loginRe", result);
        res.putAll(sessionMap);
        return res;
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> logout() {
        Map<String, Object> res = new HashMap<String, Object>();
        this.clearSession(request);
        res.put("status", 1);
        return res;
    }

    @RequestMapping(value = "/insertUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertUserInfo(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String studentNumber,
            @RequestParam(required = true) Integer teacher,
            @RequestParam(required = true) Integer admin) {
        Map<String, Object> res = new HashMap<String, Object>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setIsTeacher(teacher);
        userInfo.setIsAdmin(admin);
        UserInfoInsertRe result = userInfoService.insert(userInfo, studentNumber);
        res.put("userInfoInsertRe", result);
        return res;
    }
}
