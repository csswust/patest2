package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2017/11/19.
 */
@Repository
public class UserInfoDaoImpl extends CommonMapper<UserInfo, BaseQuery> implements UserInfoDao {
    private static Logger log = LoggerFactory.getLogger(UserInfoDaoImpl.class);

    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.UserInfoDao.";
    }

    @Override
    public void insertInit(UserInfo record, Date date) {
        record.setUserId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
    }

    @Override
    public void updatInit(UserInfo record, Date date) {
        record.setModifyTime(date);
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

    @Override
    public int deleteByExamId(Integer examId) {
        if (examId == null) {
            return 0;
        }
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("examId", examId);
            return getSqlSession().delete(getPackage() + "deleteByExamId", param);
        } catch (Exception e) {
            log.error("UserInfoDaoImpl.deleteByExamId({}) error: {}", examId, e);
            return 0;
        }
    }
}
