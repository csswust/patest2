package com.csswust.patest2.service.result;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.UserProfile;

import java.util.List;

/**
 * Created by 972536780 on 2018/3/16.
 */
public class UserProfileLoadRe extends APIResult {
    private List<UserProfile> errorList;
    private UserProfile error;

    public List<UserProfile> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<UserProfile> errorList) {
        this.errorList = errorList;
    }

    public UserProfile getError() {
        return error;
    }

    public void setError(UserProfile error) {
        this.error = error;
    }
}
