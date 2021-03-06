package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserLoginLogDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserLoginLog;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.UserInfoService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.result.LoginRe;
import com.csswust.patest2.service.result.UserInfoInsertRe;
import com.csswust.patest2.utils.CipherUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

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
            result.setStatus(-1);
            result.setDesc("学号不能为空");
            return result;
        }
        UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
        if (userProfile == null) {
            result.setStatus(-2);
            result.setDesc("未找到对应学号的学生");
            return result;
        }
        userInfo.setUserProfileId(userProfile.getUseProId());
        userInfo.setIsActive(1);
        try {
            userInfo.setPassword(CipherUtil.encode(userInfo.getPassword()));
        } catch (Exception e) {
            log.error("CipherUtil.encode passwor : {} derror: {}", userInfo.getPassword(), e);
            result.setStatus(-3);
            result.setDesc("密码加密失败");
            return result;
        }
        int status = userInfoDao.insertSelective(userInfo);
        result.setStatus(status);
        return result;
    }

    @Override
    public APIResult update(UserInfo userInfo, String studentNumber) {
        APIResult result = new APIResult();
        if (StringUtils.isBlank(studentNumber)) {
            result.setStatus(-1);
            result.setDesc("学号不能为空");
            return result;
        }
        UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
        if (userProfile == null) {
            result.setStatus(-2);
            result.setDesc("未找到对应学号的学生");
            return result;
        }
        userInfo.setUserProfileId(userProfile.getUseProId());
        try {
            if (StringUtils.isNotBlank(userInfo.getPassword())) {
                userInfo.setPassword(CipherUtil.encode(userInfo.getPassword()));
            } else {
                userInfo.setPassword(null);// 防止无意更改
            }
        } catch (Exception e) {
            log.error("CipherUtil.encode passwor : {} derror: {}", userInfo.getPassword(), e);
            result.setStatus(-3);
            result.setDesc("密码加密失败");
            return result;
        }
        int status = userInfoDao.updateByPrimaryKeySelective(userInfo);
        result.setStatus(status);
        return result;
    }

    @Override
    public LoginRe login(String username, String password, String IP) {
        LoginRe loginRe = new LoginRe();
        try {
            password = CipherUtil.encode(password);
        } catch (Exception e) {
            log.error("CipherUtil.encode password : {} derror: {}", password, e);
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
        int isLoginLock = Config.getToInt(SiteKey.IS_LOGIN_LOCK, SiteKey.IS_LOGIN_LOCK_DE);
        // 判断用户是否active，并且not lock
        if (isLoginLock == 1 && currUser.getIsLock() != 0) {
            loginRe.setStatus(-3);
            loginRe.setDesc("账号被锁定! 请联系管理员解锁");
            return loginRe;
        }
        if (currUser.getIsActive() != 1) {
            loginRe.setStatus(-4);
            loginRe.setDesc("你的账号未激活！请联系管理员激活");
            return loginRe;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(currUser.getExamId());
        loginRe.setExamInfo(examInfo);
        if (examInfo == null && currUser.getIsAdmin() != 1 && currUser.getIsTeacher() != 1) {
            loginRe.setStatus(-11);
            loginRe.setDesc("未关联考试");
            return loginRe;
        }
        // 当登录的是学生时，需要判断ip是否在限定范围内
        int isLimitIP = Config.getToInt(SiteKey.IS_LIMIT_IP, SiteKey.IS_LIMIT_IP_DE);
        if (isLimitIP == 1 && currUser.getIsAdmin() != 1 && currUser.getIsTeacher() != 1) {
            if (!"0:0:0:0:0:0:0:1".equals(IP)) {
                String ips = null;
                ips = examInfo == null ? "" : examInfo.getAllowIp();
                boolean temp = judgeIp(ips, IP);
                if (!temp) {
                    loginRe.setStatus(-6);
                    loginRe.setDesc("登录ip异常");
                    return loginRe;
                }
            }
        }
        // 登录成功
        loginRe.setStatus(1);
        UserProfile userProfile = userProfileDao.selectByPrimaryKey(currUser.getUserProfileId());
        loginRe.setUserProfile(userProfile);

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
            recordUser.setIsLock(1);
        }
        userInfoDao.updateByPrimaryKeySelective(recordUser);
        return loginRe;
    }

    private boolean judgeIp(String allowIp, String ip) {
        if (StringUtils.isBlank(allowIp)) return true;
        if (StringUtils.isBlank(ip)) return false;
        String[] strs = allowIp.split(",");
        for (int i = 0; i < strs.length; i++) {
            String pattern = strs[i];
            if (StringUtils.isBlank(pattern)) continue;
            if (pattern.equals("*")) return true;
            try {
                boolean isMatch = Pattern.matches(pattern, ip);
                if (isMatch) return true;
            } catch (Exception e) {
                log.error("Pattern.matches({},{}) error: {}", pattern, ip, e.getMessage());
            }
        }
        return false;
    }

    /*public static void main(String[] args) {
        System.out.println(judgeIp("222.196.35.220", "222.196.35.220"));
        System.out.println(judgeIp("222.196.35.220", "222.196.35.221"));
        System.out.println(judgeIp("222.196.35.22\\d", "222.196.35.221"));
    }*/
}

