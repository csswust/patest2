package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ExamPaperDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamPaperDaoImpl extends CommonMapper implements ExamPaperDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ExamPaperDao.";

    @Override
    public int deleteByPrimaryKey(Integer exaPapId) {
        if (exaPapId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", exaPapId);
    }

    @Override
    public int insert(ExamPaper record) {
        if (record == null) {
            return 0;
        }
        record.setExaPapId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ExamPaper record) {
        if (record == null) {
            return 0;
        }
        record.setExaPapId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ExamPaper selectByPrimaryKey(Integer exaPapId) {
        if (exaPapId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", exaPapId);
    }

    @Override
    public int updateByPrimaryKeySelective(ExamPaper record) {
        if (record == null || record.getExaPapId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(ExamPaper record) {
        if (record == null || record.getExaPapId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
