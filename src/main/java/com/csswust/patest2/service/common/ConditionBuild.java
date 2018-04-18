package com.csswust.patest2.service.common;

import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;
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
        if (StringUtils.isNotBlank(studentNumber)) {
            UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
            if (userProfile == null) {
                baseQuery.setCustom("userIds", new ArrayList<Integer>());
            } else {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserProfileId(userProfile.getUseProId());
                List<UserInfo> userInfoList = userInfoDao.selectByCondition(userInfo, new BaseQuery());
                List<Integer> userIds = getFieldByList(userInfoList, "userId", UserInfo.class);
                baseQuery.setCustom("userIds", userIds);
            }
        }
    }
}
