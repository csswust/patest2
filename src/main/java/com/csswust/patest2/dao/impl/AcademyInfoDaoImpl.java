package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.entity.AcademyInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class AcademyInfoDaoImpl extends CommonMapper<AcademyInfo, BaseQuery> implements AcademyInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.AcademyInfoDao.";
    }

    @Override
    public void insertInit(AcademyInfo record) {
        record.setAcaId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(AcademyInfo record) {
        record.setModifyTime(new Date());
    }
}
