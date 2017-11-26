package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ExamInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamInfoDaoImpl extends CommonMapper implements ExamInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ExamInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer examId) {
        if (examId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", examId);
    }

    @Override
    public int insert(ExamInfo record) {
        if (record == null) {
            return 0;
        }
        record.setExamId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ExamInfo record) {
        if (record == null) {
            return 0;
        }
        record.setExamId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ExamInfo selectByPrimaryKey(Integer examId) {
        if (examId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", examId);
    }

    @Override
    public int updateByPrimaryKeySelective(ExamInfo record) {
        if (record == null || record.getExamId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(ExamInfo record) {
        if (record == null || record.getExamId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(ExamInfo record) {
        if (record == null || record.getExamId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
