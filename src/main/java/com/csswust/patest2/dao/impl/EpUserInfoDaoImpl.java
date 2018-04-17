package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.entity.EpUserInfo;
import com.csswust.patest2.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class EpUserInfoDaoImpl extends CommonMapper<EpUserInfo, BaseQuery> implements EpUserInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.EpUserInfoDao.";
    }

    @Override
    public void insertInit(EpUserInfo record, Date date) {
        record.setUserId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(EpUserInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public EpUserInfo selectByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        EpUserInfo record = new EpUserInfo();
        record.setUsername(username);
        List<EpUserInfo> result = this.selectByCondition(record, new BaseQuery(1, 1));
        return (result != null && result.size() != 0) ? result.get(0) : null;
    }

    @Override
    public EpUserInfo selectByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        EpUserInfo record = new EpUserInfo();
        record.setEmail(email);
        List<EpUserInfo> result = this.selectByCondition(record, new BaseQuery(1, 1));
        return (result != null && result.size() != 0) ? result.get(0) : null;
    }
}
