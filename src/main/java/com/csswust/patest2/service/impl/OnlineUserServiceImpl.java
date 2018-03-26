package com.csswust.patest2.service.impl;

import com.csswust.patest2.dao.ExamPaperDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserLoginLogDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserLoginLog;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.listener.OnlineListener;
import com.csswust.patest2.service.OnlineUserService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.result.OnlineListRe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/22.
 */
@Service
public class OnlineUserServiceImpl extends BaseService implements OnlineUserService {
    private static Logger log = LoggerFactory.getLogger(OnlineUserServiceImpl.class);

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private UserLoginLogDao userLoginLogDao;

    void getEffectiveSession(List<Integer> userIdList, List<String> sessionIdList) {
        Map<String, HttpSession> map = OnlineListener.onlineMap;
        for (Map.Entry<String, HttpSession> entry : map.entrySet()) {
            String sessionId = entry.getKey();
            HttpSession session = entry.getValue();
            if (sessionId == null || session == null) {
                continue;
            }
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                userIdList.add(userId);
                sessionIdList.add(sessionId);
            }
        }
    }

    @Override
    public OnlineListRe getOnlineList(Integer page, Integer rows) {
        List<Integer> userIdList = new ArrayList<>();
        List<String> sessionIdList = new ArrayList<>();
        getEffectiveSession(userIdList, sessionIdList);
        int start = 0, end = userIdList.size();
        if (page != null && rows != null) {
            start = (page - 1) * rows;
            end = Math.min(end, start + rows);
        }
        List<Integer> ids = userIdList.subList(start, end);
        List<String> sessionIds = sessionIdList.subList(start, end);
        List<UserInfo> userInfoList = selectRecordByIds(ids,
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        List<ExamPaper> examPaperList = new ArrayList<>();
        List<UserLoginLog> userLoginLogList = new ArrayList<>();
        UserLoginLog userLoginLogCondition = new UserLoginLog();
        ExamPaper examPaperCondition = new ExamPaper();
        for (int i = 0; i < userInfoList.size(); i++) {
            ExamPaper examPaper = new ExamPaper();
            UserLoginLog userLoginLog = new UserLoginLog();
            examPaperList.add(examPaper);
            userLoginLogList.add(userLoginLog);
            UserInfo userInfo = userInfoList.get(i);
            if (userInfo == null || userInfo.getUserId() == null) {
                continue;
            }
            userLoginLogCondition.setUserId(userInfo.getUserId());
            List<UserLoginLog> list = userLoginLogDao.selectByCondition(
                    userLoginLogCondition, new BaseQuery(1, 1));
            if (list != null && list.size() != 0) {
                userLoginLog.setLoginIp(list.get(0).getLoginIp());
            }
            if (userInfo.getExamId() == null) {
                continue;
            }
            examPaperCondition.setUserId(userInfo.getUserId());
            examPaperCondition.setExamId(userInfo.getExamId());
            List<ExamPaper> list1 = examPaperDao.selectByCondition(
                    examPaperCondition, new BaseQuery(1, 1));
            if (list1 != null && list1.size() != 0) {
                examPaper.setClassroom(list1.get(0).getClassroom());
            }
        }
        OnlineListRe onlineListRe = new OnlineListRe();
        onlineListRe.setUserInfoList(userInfoList);
        onlineListRe.setUserProfileList(userProfileList);
        onlineListRe.setSessionIdList(sessionIds);
        onlineListRe.setExamPaperList(examPaperList);
        onlineListRe.setUserLoginLogList(userLoginLogList);
        onlineListRe.setTotal(userIdList.size());
        return onlineListRe;
    }

    @Override
    public List<String> judgeOnline(List<Integer> userIds) {
        List<String> judgeResult = new ArrayList<>();
        if (userIds == null || userIds.size() == 0) return judgeResult;
        List<Integer> userIdList = new ArrayList<>();
        List<String> sessionIdList = new ArrayList<>();
        getEffectiveSession(userIdList, sessionIdList);
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < userIdList.size(); i++) {
            map.put(userIdList.get(i), sessionIdList.get(i));
        }
        for (Integer item : userIds) {
            judgeResult.add(map.get(item));
        }
        return judgeResult;
    }
}
