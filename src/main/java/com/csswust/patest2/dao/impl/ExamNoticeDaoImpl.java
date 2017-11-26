package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ExamNotice;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ExamNoticeDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamNoticeDaoImpl extends CommonMapper implements ExamNoticeDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ExamNoticeDao.";

    @Override
    public int deleteByPrimaryKey(Integer exaNotId) {
        if (exaNotId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", exaNotId);
    }

    @Override
    public int insert(ExamNotice record) {
        if (record == null) {
            return 0;
        }
        record.setExaNotId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ExamNotice record) {
        if (record == null) {
            return 0;
        }
        record.setExaNotId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ExamNotice selectByPrimaryKey(Integer exaNotId) {
        if (exaNotId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", exaNotId);
    }

    @Override
    public int updateByPrimaryKeySelective(ExamNotice record) {
        if (record == null || record.getExaNotId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(ExamNotice record) {
        if (record == null || record.getExaNotId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(ExamNotice record) {
        if (record == null || record.getExaNotId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
