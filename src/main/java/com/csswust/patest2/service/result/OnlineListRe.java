package com.csswust.patest2.service.result;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserLoginLog;
import com.csswust.patest2.entity.UserProfile;

import java.util.List;

/**
 * Created by 972536780 on 2018/3/22.
 */
public class OnlineListRe extends APIResult {
    private Integer total;
    private List<UserInfo> userInfoList;
    private List<UserProfile> userProfileList;
    private List<String> sessionIdList;
    private List<ExamPaper> examPaperList;
    private List<UserLoginLog> userLoginLogList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public List<UserProfile> getUserProfileList() {
        return userProfileList;
    }

    public void setUserProfileList(List<UserProfile> userProfileList) {
        this.userProfileList = userProfileList;
    }

    public List<String> getSessionIdList() {
        return sessionIdList;
    }

    public void setSessionIdList(List<String> sessionIdList) {
        this.sessionIdList = sessionIdList;
    }

    public List<ExamPaper> getExamPaperList() {
        return examPaperList;
    }

    public void setExamPaperList(List<ExamPaper> examPaperList) {
        this.examPaperList = examPaperList;
    }

    public List<UserLoginLog> getUserLoginLogList() {
        return userLoginLogList;
    }

    public void setUserLoginLogList(List<UserLoginLog> userLoginLogList) {
        this.userLoginLogList = userLoginLogList;
    }
}
