package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.JudgerInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.JudgerInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class JudgerInfoDaoImpl extends CommonMapper implements JudgerInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.JudgerInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer judId) {
        if (judId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", judId);
    }

    @Override
    public int insert(JudgerInfo record) {
        if (record == null) {
            return 0;
        }
        record.setJudId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(JudgerInfo record) {
        if (record == null) {
            return 0;
        }
        record.setJudId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public JudgerInfo selectByPrimaryKey(Integer judId) {
        if (judId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", judId);
    }

    @Override
    public int updateByPrimaryKeySelective(JudgerInfo record) {
        if (record == null || record.getJudId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(JudgerInfo record) {
        if (record == null || record.getJudId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
