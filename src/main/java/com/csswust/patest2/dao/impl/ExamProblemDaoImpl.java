package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ExamProblem;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ExamProblemDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamProblemDaoImpl extends CommonMapper implements ExamProblemDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ExamProblemDao.";

    @Override
    public int deleteByPrimaryKey(Integer exaProId) {
        if (exaProId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", exaProId);
    }

    @Override
    public int insert(ExamProblem record) {
        if (record == null) {
            return 0;
        }
        record.setExaProId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ExamProblem record) {
        if (record == null) {
            return 0;
        }
        record.setExaProId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ExamProblem selectByPrimaryKey(Integer exaProId) {
        if (exaProId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", exaProId);
    }

    @Override
    public int updateByPrimaryKeySelective(ExamProblem record) {
        if (record == null || record.getExaProId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(ExamProblem record) {
        if (record == null || record.getExaProId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
