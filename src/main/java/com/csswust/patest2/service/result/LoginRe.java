package com.csswust.patest2.service.result;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;

/**
 * Created by 972536780 on 2018/3/12.
 */
public class LoginRe extends APIResult {
    private UserInfo currUser;
    private UserProfile userProfile;
    private ExamInfo examInfo;

    public UserInfo getCurrUser() {
        return currUser;
    }

    public void setCurrUser(UserInfo currUser) {
        this.currUser = currUser;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public ExamInfo getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ExamInfo examInfo) {
        this.examInfo = examInfo;
    }
}
