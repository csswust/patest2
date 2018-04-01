package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.CourseInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.CourseInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class CourseInfoDaoImpl extends CommonMapper<CourseInfo, BaseQuery> implements CourseInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.CourseInfoDao.";
    }

    @Override
    public void insertInit(CourseInfo record, Date date) {
        record.setCouId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
    }

    @Override
    public void updatInit(CourseInfo record, Date date) {
        record.setModifyTime(date);
    }
}
