package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.LevelTypeDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.LevelType;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class LevelTypeDaoImpl extends CommonMapper<LevelType, BaseQuery> implements LevelTypeDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.LevelTypeDao.";
    }

    @Override
    public void insertInit(LevelType record, Date date) {
        record.setLevTypId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(LevelType record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
