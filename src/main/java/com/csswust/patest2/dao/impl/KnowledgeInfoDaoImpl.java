package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.KnowledgeInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.KnowledgeInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class KnowledgeInfoDaoImpl extends CommonMapper implements KnowledgeInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.KnowledgeInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer knowId) {
        if (knowId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", knowId);
    }

    @Override
    public int insert(KnowledgeInfo record) {
        if (record == null) {
            return 0;
        }
        record.setKnowId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(KnowledgeInfo record) {
        if (record == null) {
            return 0;
        }
        record.setKnowId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public KnowledgeInfo selectByPrimaryKey(Integer knowId) {
        if (knowId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", knowId);
    }

    @Override
    public int updateByPrimaryKeySelective(KnowledgeInfo record) {
        if (record == null || record.getKnowId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(KnowledgeInfo record) {
        if (record == null || record.getKnowId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
