package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ExamParam;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ExamParamDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamParamDaoImpl extends CommonMapper implements ExamParamDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ExamParamDao.";

    @Override
    public int deleteByPrimaryKey(Integer exaParId) {
        if (exaParId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", exaParId);
    }

    @Override
    public int insert(ExamParam record) {
        if (record == null) {
            return 0;
        }
        record.setExaParId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ExamParam record) {
        if (record == null) {
            return 0;
        }
        record.setExaParId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ExamParam selectByPrimaryKey(Integer exaParId) {
        if (exaParId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", exaParId);
    }

    @Override
    public int updateByPrimaryKeySelective(ExamParam record) {
        if (record == null || record.getExaParId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(ExamParam record) {
        if (record == null || record.getExaParId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
