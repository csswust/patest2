package com.csswust.patest2.service.impl;

import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public int insert(UserInfo userInfo) {
        int result = userInfoDao.insertSelective(userInfo);
        return result;
    }
}
