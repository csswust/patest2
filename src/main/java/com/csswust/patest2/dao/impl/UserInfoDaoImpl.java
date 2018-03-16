package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    @Override
    public UserInfo selectByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        UserInfo record = new UserInfo();
        record.setUsername(username);
        List<UserInfo> result = this.selectByCondition(record, new BaseQuery(1, 1));
        return (result != null && result.size() != 0) ? result.get(0) : null;
    }
}
