package com.csswust.patest2.service.common;

import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;

/**
 * Created by 972536780 on 2018/4/15.
 */
@Component
public class ConditionBuild extends BaseService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;


    public void buildExamPaper(BaseQuery baseQuery, ExamPaper examPaper,
                               String userName, String studentNumber) {
        if (StringUtils.isNotBlank(userName)) {
            UserInfo userInfo = userInfoDao.selectByUsername(userName);
            if (userInfo != null) {
                examPaper.setUserId(userInfo.getUserId());
            } else {
                examPaper.setUserId(-1);
            }
        }
        buildByStudentNumber(baseQuery, studentNumber);
    }

    public void buildUserLoginLog(BaseQuery baseQuery, UserLoginLog loginLog,
                                  String userName, String studentNumber) {
        if (StringUtils.isNotBlank(userName)) {
            UserInfo userInfo = userInfoDao.selectByUsername(userName);
            loginLog.setUserId(userInfo == null ? -1 : userInfo.getUserId());
        }
        buildByStudentNumber(baseQuery, studentNumber);
    }

    public void buildOperateLog(BaseQuery baseQuery, OperateLog operateLog,
                                String userName, String studentNumber) {
        if (StringUtils.isNotBlank(userName)) {
            UserInfo userInfo = userInfoDao.selectByUsername(userName);
            operateLog.setUserId(userInfo == null ? -1 : userInfo.getUserId());
        }
        buildByStudentNumber(baseQuery, studentNumber);
    }

    private void buildByStudentNumber(BaseQuery query, String studentNumber) {
        if (StringUtils.isNotBlank(studentNumber)) {
            UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
            if (userProfile == null) {
                query.setCustom("userIds", new ArrayList<Integer>());
            } else {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserProfileId(userProfile.getUseProId());
                List<UserInfo> userInfoList = userInfoDao.selectByCondition(userInfo, new BaseQuery());
                List<Integer> userIds = getFieldByList(userInfoList, "userId", UserInfo.class);
                query.setCustom("userIds", userIds);
            }
        }
    }
}
