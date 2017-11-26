package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.entity.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserProfileDaoImpl extends CommonMapper<UserProfile, BaseQuery> implements UserProfileDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.UserProfileDao.";
    }

    @Override
    public void insertInit(UserProfile record) {
        record.setUseProId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(UserProfile record) {
        record.setModifyTime(new Date());
    }
}
