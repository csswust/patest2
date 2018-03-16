package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.LevelTypeDao;
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
    public void insertInit(LevelType record) {
        record.setLevTypId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(LevelType record) {
        record.setModifyTime(new Date());
    }
}
