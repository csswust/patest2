package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.entity.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    @Override
    public UserProfile selectByStudentNumber(String studentNumber) {
        if (StringUtils.isBlank(studentNumber)) {
            return null;
        }
        UserProfile record = new UserProfile();
        record.setStudentNumber(studentNumber);
        List<UserProfile> result = this.selectByCondition(record, new BaseQuery(1, 1));
        return (result != null && result.size() != 0) ? result.get(0) : null;
    }
}
