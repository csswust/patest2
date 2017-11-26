package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.AcademyInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.AcademyInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class AcademyInfoDaoImpl extends CommonMapper implements AcademyInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.AcademyInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer acaId) {
        if (acaId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", acaId);
    }

    @Override
    public int insert(AcademyInfo record) {
        if (record == null) {
            return 0;
        }
        record.setAcaId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(AcademyInfo record) {
        if (record == null) {
            return 0;
        }
        record.setAcaId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public AcademyInfo selectByPrimaryKey(Integer acaId) {
        if (acaId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", acaId);
    }

    @Override
    public int updateByPrimaryKeySelective(AcademyInfo record) {
        if (record == null || record.getAcaId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(AcademyInfo record) {
        if (record == null || record.getAcaId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
