package com.csswust.patest2.test.dao;

import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends JunitBaseServiceDaoTest {
    @Autowired
    private UserInfoDao userInfoDao;

    @Test
    public void insertSelective() {
        UserInfo user = new UserInfo();
        user.setUsername("zhansan1");
        user.setPassword("1234");
        System.out.println(userInfoDao.insertSelective(user));
    }

    @Test
    public void deleteByPrimaryKey() {
        System.out.println(userInfoDao.deleteByPrimaryKey(18613));
    }
}
