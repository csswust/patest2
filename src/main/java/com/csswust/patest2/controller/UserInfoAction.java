package com.csswust.patest2.controller;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.paramJudge.StringCallBack;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.UserInfoService;
import com.csswust.patest2.service.result.LoginRe;
import com.csswust.patest2.service.result.UserInfoInsertRe;
import org.apache.commons.lang3.StringUtils;
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

@RestController
@RequestMapping("/userInfo")
public class UserInfoAction extends BaseAction {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private UserInfoDao userInfoDao;

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

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            UserInfo userInfo,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        userInfo = paramVerificate(userInfo, new StringCallBack());
        Map<String, Object> res = new HashMap<>();
        BaseQuery baseQuery = new BaseQuery();
        if (StringUtils.isNotBlank(realName)) {
            UserProfile userProfile = new UserProfile();
            userProfile.setRealName(realName);
            List<UserProfile> userProfileList = userProfileDao.selectByCondition(userProfile, new BaseQuery());
            List<Integer> ids = getFieldByList(userProfileList, "useProId", UserProfile.class);
            baseQuery.setCustom("userProfileIds", ids);
        }
        Integer total = userInfoDao.selectByConditionGetCount(userInfo, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<UserInfo> userInfoList = userInfoDao.selectByCondition(userInfo, baseQuery);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        res.put("total", total);
        res.put("userInfoList", userInfoList);
        res.put("userProfileList", userProfileList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String studentNumber,
            @RequestParam(required = true) Integer teacher,
            @RequestParam(required = true) Integer admin) {
        Map<String, Object> res = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setIsTeacher(teacher);
        userInfo.setIsAdmin(admin);
        UserInfoInsertRe result = userInfoService.insert(userInfo, studentNumber);
        res.put("userInfoInsertRe", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(
            UserInfo userInfo,
            @RequestParam(required = true) String studentNumber) {
        Map<String, Object> res = new HashMap<>();
        APIResult result = userInfoService.update(userInfo, studentNumber);
        res.put("APIResult", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = userInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
