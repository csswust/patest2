package com.csswust.patest2.service.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserLoginLogDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserLoginLog;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.BaseService;
import com.csswust.patest2.service.UserInfoService;
import com.csswust.patest2.service.result.LoginRe;
import com.csswust.patest2.service.result.UserInfoInsertRe;
import com.csswust.patest2.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserInfoServiceImpl extends BaseService implements UserInfoService {
    private static Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private UserLoginLogDao userLoginLogDao;

    @Override
    public UserInfoInsertRe insert(UserInfo userInfo, String studentNumber) {
        UserInfoInsertRe result = new UserInfoInsertRe();
        if (StringUtils.isBlank(studentNumber)) {
            result.setDesc("学号不能为空");
            return result;
        }
        UserProfile userProfileCondition = new UserProfile();
        userProfileCondition.setStudentNumber(studentNumber);
        List<UserProfile> userProfileList = userProfileDao.selectByCondition(userProfileCondition, new BaseQuery(1, 1));
        if (userProfileList == null || userProfileList.size() == 0) {
            result.setDesc("未找到对应学号的学生");
            return result;
        }
        UserProfile userProfile = userProfileList.get(0);
        userInfo.setUserProfileId(userProfile.getUseProId());
        userInfo.setIsActive(1);
        try {
            userInfo.setPassword(MD5Util.encode(userInfo.getPassword()));
        } catch (Exception e) {
            log.error("MD5Util.encode passwor : {} derror: {}", userInfo.getPassword(), e);
            result.setDesc("密码加密失败");
            return result;
        }
        int status = userInfoDao.insertSelective(userInfo);
        result.setStatus(status);
        return result;
    }

    @Override
    public LoginRe login(String username, String password, String IP) {
        LoginRe loginRe = new LoginRe();
        try {
            password = MD5Util.encode(password);
        } catch (Exception e) {
            log.error("MD5Util.encode password : {} derror: {}", password, e);
            loginRe.setDesc("密码加密失败");
            return loginRe;
        }
        UserInfo currUser = userInfoDao.selectByUsername(username);
        if (currUser == null) {
            loginRe.setStatus(-1);
            loginRe.setDesc("用户不存在");
            return loginRe;
        }
        loginRe.setCurrUser(currUser);
        String rightPass = currUser.getPassword();
        if (StringUtils.isBlank(rightPass) || !rightPass.equals(password)) {
            loginRe.setStatus(-2);
            loginRe.setDesc("密码错误");
            return loginRe;
        }
        // 判断用户是否active，并且not lock
        if (currUser.getIsLock() != 0) {
            loginRe.setStatus(-3);
            loginRe.setDesc("账号被锁定! 请联系管理员解锁");
            return loginRe;
        }
        if (currUser.getIsActive() != 1) {
            loginRe.setStatus(-4);
            loginRe.setDesc("你的账号未激活！请联系管理员激活");
            return loginRe;
        }
        // 当登录的是学生时，需要判断ip是否在限定范围内
        if (currUser.getIsAdmin() != 1 && currUser.getIsTeacher() != 1) {
            if (!"0:0:0:0:0:0:0:1".equals(IP)) {
                String ips = "";
                ExamInfo examInfo = examInfoDao.selectByPrimaryKey(currUser.getExamId());
                if (examInfo != null) {
                    ips = examInfo.getAllowIp();
                }/* else {
                    loginRe.setStatus(-5);
                    loginRe.setDesc("你的账户未激活，请联系管理员激活");
                    return loginRe;
                }*/
                if (ips != null) {
                    String[] strs = ips.split(",");
                    if (!(Arrays.asList(strs).contains(IP))) {
                        loginRe.setStatus(-6);
                        loginRe.setDesc("登录ip异常");
                        return loginRe;
                    }
                }
            }
        }
        // 登录成功
        loginRe.setStatus(1);
        UserProfile userProfile = userProfileDao.selectByPrimaryKey(currUser.getUserProfileId());
        loginRe.setUserProfile(userProfile);
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(currUser.getExamId());
        loginRe.setExamInfo(examInfo);
        //插入登录日志
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(currUser.getUserId());
        userLoginLog.setLoginIp(IP);
        userLoginLogDao.insertSelective(userLoginLog);
        //更新用户的最后登录时间
        UserInfo recordUser = new UserInfo();
        recordUser.setLastLogin(Calendar.getInstance().getTime());
        recordUser.setUserId(currUser.getUserId());
        if (currUser.getIsAdmin() != 1 && currUser.getIsTeacher() != 1) {
            // 非管理员登录后就不允许登录
            currUser.setIsLock(1);
        }
        userInfoDao.updateByPrimaryKeySelective(recordUser);
        return loginRe;
    }
}

