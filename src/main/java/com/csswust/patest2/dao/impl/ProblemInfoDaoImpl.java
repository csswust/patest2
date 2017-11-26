package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ProblemInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ProblemInfoDaoImpl extends CommonMapper implements ProblemInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ProblemInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer probId) {
        if (probId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", probId);
    }

    @Override
    public int insert(ProblemInfo record) {
        if (record == null) {
            return 0;
        }
        record.setProbId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ProblemInfo record) {
        if (record == null) {
            return 0;
        }
        record.setProbId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ProblemInfo selectByPrimaryKey(Integer probId) {
        if (probId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", probId);
    }

    @Override
    public int updateByPrimaryKeySelective(ProblemInfo record) {
        if (record == null || record.getProbId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(ProblemInfo record) {
        if (record == null || record.getProbId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(ProblemInfo record) {
        if (record == null || record.getProbId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
