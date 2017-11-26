package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.MajorInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.MajorInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class MajorInfoDaoImpl extends CommonMapper implements MajorInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.MajorInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer majId) {
        if (majId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", majId);
    }

    @Override
    public int insert(MajorInfo record) {
        if (record == null) {
            return 0;
        }
        record.setMajId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(MajorInfo record) {
        if (record == null) {
            return 0;
        }
        record.setMajId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public MajorInfo selectByPrimaryKey(Integer majId) {
        if (majId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", majId);
    }

    @Override
    public int updateByPrimaryKeySelective(MajorInfo record) {
        if (record == null || record.getMajId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(MajorInfo record) {
        if (record == null || record.getMajId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
