package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.JudgerInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.JudgerInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class JudgerInfoDaoImpl extends CommonMapper<JudgerInfo, BaseQuery> implements JudgerInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.JudgerInfoDao.";
    }

    @Override
    public void insertInit(JudgerInfo record) {
        record.setJudId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(JudgerInfo record) {
        record.setModifyTime(new Date());
    }
}
