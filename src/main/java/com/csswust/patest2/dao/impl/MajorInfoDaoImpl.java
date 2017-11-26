package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.MajorInfoDao;
import com.csswust.patest2.entity.MajorInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class MajorInfoDaoImpl extends CommonMapper<MajorInfo, BaseQuery> implements MajorInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.MajorInfoDao.";
    }

    @Override
    public void insertInit(MajorInfo record) {
        record.setMajId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(MajorInfo record) {
        record.setModifyTime(new Date());
    }
}
