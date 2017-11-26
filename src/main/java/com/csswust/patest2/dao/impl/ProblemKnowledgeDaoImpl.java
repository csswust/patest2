package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ProblemKnowledge;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ProblemKnowledgeDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ProblemKnowledgeDaoImpl extends CommonMapper implements ProblemKnowledgeDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ProblemKnowledgeDao.";

    @Override
    public int deleteByPrimaryKey(Integer proKnoId) {
        if (proKnoId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", proKnoId);
    }

    @Override
    public int insert(ProblemKnowledge record) {
        if (record == null) {
            return 0;
        }
        record.setProKnoId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ProblemKnowledge record) {
        if (record == null) {
            return 0;
        }
        record.setProKnoId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ProblemKnowledge selectByPrimaryKey(Integer proKnoId) {
        if (proKnoId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", proKnoId);
    }

    @Override
    public int updateByPrimaryKeySelective(ProblemKnowledge record) {
        if (record == null || record.getProKnoId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(ProblemKnowledge record) {
        if (record == null || record.getProKnoId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
