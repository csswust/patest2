package com.csswust.patest2.service;

import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.service.result.LoginRe;
import com.csswust.patest2.service.result.UserInfoInsertRe;

public interface UserInfoService {
    UserInfoInsertRe insert(UserInfo userInfo, String studentNumber);

    LoginRe login(String username, String password, String IP);
}
