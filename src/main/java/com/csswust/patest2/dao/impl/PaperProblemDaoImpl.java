package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.PaperProblem;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.PaperProblemDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class PaperProblemDaoImpl extends CommonMapper implements PaperProblemDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.PaperProblemDao.";

    @Override
    public int deleteByPrimaryKey(Integer papProId) {
        if (papProId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", papProId);
    }

    @Override
    public int insert(PaperProblem record) {
        if (record == null) {
            return 0;
        }
        record.setPapProId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(PaperProblem record) {
        if (record == null) {
            return 0;
        }
        record.setPapProId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public PaperProblem selectByPrimaryKey(Integer papProId) {
        if (papProId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", papProId);
    }

    @Override
    public int updateByPrimaryKeySelective(PaperProblem record) {
        if (record == null || record.getPapProId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(PaperProblem record) {
        if (record == null || record.getPapProId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
