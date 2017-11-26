package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.entity.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by 972536780 on 2017/11/19.
 */
@Repository
public class UserInfoDaoImpl extends CommonMapper<UserInfo, BaseQuery> implements UserInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.UserInfoDao.";
    }

    @Override
    public void insertInit(UserInfo record) {
        record.setUserId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(UserInfo record) {
        record.setModifyTime(new Date());
    }
}
