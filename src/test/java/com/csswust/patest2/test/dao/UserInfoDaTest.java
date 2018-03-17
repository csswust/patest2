package com.csswust.patest2.test.dao;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.AcademyInfo;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInfoDaTest extends JunitBaseServiceDaoTest {
    @Autowired
    private UserInfoDao userInfoDao;

    @Test
    public void insertSelective() {
        UserInfo userInfo = new UserInfo();
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setCustom("userProfileIds", Arrays.asList(2, 6409));
        List<UserInfo> result = userInfoDao.selectByCondition(userInfo, baseQuery);
        System.out.println(JSON.toJSONString(result));
    }
}
