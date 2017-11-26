package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.CourseInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.CourseInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class CourseInfoDaoImpl extends CommonMapper implements CourseInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.CourseInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer couId) {
        if (couId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", couId);
    }

    @Override
    public int insert(CourseInfo record) {
        if (record == null) {
            return 0;
        }
        record.setCouId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(CourseInfo record) {
        if (record == null) {
            return 0;
        }
        record.setCouId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public CourseInfo selectByPrimaryKey(Integer couId) {
        if (couId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", couId);
    }

    @Override
    public int updateByPrimaryKeySelective(CourseInfo record) {
        if (record == null || record.getCouId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(CourseInfo record) {
        if (record == null || record.getCouId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
